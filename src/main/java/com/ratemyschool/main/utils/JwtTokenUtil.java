package com.ratemyschool.main.utils;

import com.ratemyschool.main.model.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    public static final String BEARER_PREFIX = "Bearer ";
    private final String JWTSecret = UUID.randomUUID().toString();
    private static final String JWTIssuer = "com.ratemyschool.main";

    private static Date createExpirationDate(final Integer minutes) {
        return new Date(System.currentTimeMillis() + 1000L * 60 * minutes);
    }

    public String generateAccessToken(final UserData user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuer(JWTIssuer)
                .setIssuedAt(new Date())
                .claim("role", user.getAuthorities())
                .setExpiration(createExpirationDate(145))
                .signWith(SignatureAlgorithm.HS512, JWTSecret)
                .compact();
    }

    public String getUserEmail(final String JWT) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWTSecret)
                .parseClaimsJws(JWT)
                .getBody();
        return claims.getSubject();
    }

    public Boolean validateJWT(final String JWT) {
        Jwts.parser().setSigningKey(JWTSecret).parseClaimsJws(JWT);
        return true;
    }
}
