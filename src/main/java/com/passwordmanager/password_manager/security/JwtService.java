package com.passwordmanager.password_manager.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.passwordmanager.password_manager.model.User;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${jwt.expiration}")
  private int expirationTime;
  @Value("${jwt.secret}")
  private String jwtSecret;

  public JwtService() {

  }

  //TODO: BUG HERE -> It is not 24h as expiration it is setting years
  public String generateToken(User user) {
    Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(Instant.now().plus(expirationTime, ChronoUnit.SECONDS)))
        .signWith(secretKey, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return getJwtParser()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean isTokenValid(String token, String username) {
    return extractUsername(token).equals(username);
  }

  public boolean isExpired(String token) {
    return getJwtParser()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }

  public Date getExpirationDate(String token) {
    return getJwtParser()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
  }

  private JwtParser getJwtParser() {
    Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build();
  }

  // Add token parsing capability
  public Claims parseToken(String token) {
    return getJwtParser()
            .parseClaimsJws(token)
            .getBody();
  }

  // Specific method to extract JWT ID
  public String extractTokenId(String token) {
    return parseToken(token).getId();
  }

}
