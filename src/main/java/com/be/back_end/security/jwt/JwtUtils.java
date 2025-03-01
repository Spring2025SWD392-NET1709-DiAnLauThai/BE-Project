package com.be.back_end.security.jwt;

import java.security.Key;
import java.util.Date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.be.back_end.model.Account;
import com.be.back_end.repository.AccountRepository;
import com.be.back_end.service.AccountService.AccountDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  
  @Autowired
  private final AccountRepository accountRepository;

  public JwtUtils(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
  
  
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${be.app.jwtSecret}")
  private String jwtSecret;

  @Value("${be.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${be.app.jwtCookieName}")
  private String jwtCookie;

  @Value("${be.app.jwtRefreshExpirationMs}")
  private int jwtRefreshExpirationMs; // Refresh token expiry (milliseconds)

  public String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }


  public String generateRefreshToken(String userId) {
    return Jwts.builder()
            .setSubject(userId)  // No "R" prefix
            .claim("type", "refresh")  // Add a claim to identify refresh tokens
            .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }
  public boolean isRefreshToken(String token) {
    try {
      Claims claims = Jwts.parserBuilder().setSigningKey(key()).build()
              .parseClaimsJws(token).getBody();
      return "refresh".equals(claims.get("type", String.class));  // Check claim
    } catch (Exception e) {
      return false;
    }
  }




  public String getJwtFromCookies(HttpServletRequest request) {
    Cookie cookie = WebUtils.getCookie(request, jwtCookie);
    if (cookie != null) {
      return cookie.getValue();
    } else {
      return null;
    }
  }
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }
  public ResponseCookie generateJwtCookie(AccountDetailsImpl accountPrincipal) {
    String jwt = generateTokenFromUserID(accountPrincipal.getId());
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
        .path("/")
        .maxAge(24 * 60 * 60)
        .httpOnly(false)
        .secure(false)
        .sameSite("Lax")
        .domain("localhost")
        .build();
    return cookie;
  }

  public ResponseCookie getCleanJwtCookie() {
    ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
    return cookie;
  }


  public String getUserIdFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public boolean validateJwtToken(String authToken) {
    try {
      if(isRefreshToken(authToken))
      {
        return false;
      }
      Claims claims=Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
      return true;
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

  public String generateTokenFromUserID(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    return Jwts.builder()
        .setSubject(id)
        .claim("role", account.getRole().name())
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
    public String generateOtpToken(String email, String otp) {
    return Jwts.builder()
            .setSubject(email)
            .claim("otp", otp) // Embed OTP inside the token
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // Valid for 5 min
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }
  public String getOtpFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
            .parseClaimsJws(token)
            .getBody().get("otp", String.class);
  }


}
