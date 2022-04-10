package com.hanss.auth.security.jwt;

import com.hanss.auth.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
  private static final String AUTHORITIES_KEY = "Authorities";
  private static final String EMAIL_KEY = "email";
  private static final String USER_ID_KEY = "user_id";

  @Value("${hanss.app.jwtSecret}")
  private String jwtSecret;

  @Value("${hanss.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${hanss.app.jwtClientId}")
  private String jwtClientId;

  public String generateJwtToken(Authentication authentication, String clientId) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    String authorities = userPrincipal.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    return Jwts.builder()
        .setSubject((userPrincipal.getUsername()))
        .claim(AUTHORITIES_KEY, authorities)
        .claim(EMAIL_KEY, userPrincipal.getEmail())
        .claim(USER_ID_KEY, userPrincipal.getId())
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS512, jwtSecret+clientId)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret+jwtClientId).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret+jwtClientId).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
