package com.passwordmanager.password_manager.security;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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

  public boolean isTokenValid(String token, User user) {
    return extractUsername(token).equals(user.getUsername()) && !isExpired(token);
  }

  private boolean isExpired(String token) {
    return getJwtParser()
        .parseClaimsJws(token)
        .getBody()
        .getExpiration()
        .before(new Date());
  }

  private JwtParser getJwtParser() {
    Key secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build();
  }

}
