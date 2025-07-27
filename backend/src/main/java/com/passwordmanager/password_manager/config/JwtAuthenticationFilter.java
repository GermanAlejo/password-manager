package com.passwordmanager.password_manager.config;

import java.io.IOException;

import com.passwordmanager.password_manager.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.passwordmanager.password_manager.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsServiceImpl userDetailsService;

  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    //exclude login of asking authorization
    return request.getRequestURI().startsWith("/api/auth/login");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //skip login
    if(shouldNotFilter(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    logger.info("Getting request authorization");
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String jwt = authHeader.substring(7);

    try {
      //validate expiration first
      if(jwtService.isExpired(jwt)) {
        logger.error("Token is expired");
        throw new ExpiredJwtException(null, null, "Token Expired");
      }

      String username = jwtService.extractUsername(jwt);
      logger.info("Validating JWT");

      //Check if validation is needed
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //Final Validation against username
        if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (ExpiredJwtException e) {
      logger.error("Jwt expired");
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
      return;
    } catch (JwtException | UsernameNotFoundException e) {
      logger.error("User not found");
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
      return;
    } catch (Exception e) {
      logger.error("Unexpected error: " + e.getMessage());
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication error");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
