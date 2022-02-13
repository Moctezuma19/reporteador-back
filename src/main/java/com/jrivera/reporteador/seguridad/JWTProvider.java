package com.jrivera.reporteador.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class JWTProvider implements Serializable {
    static final Logger LOG = LoggerFactory.getLogger(JWTProvider.class);

    @Value("${jwt.security.key}")
    private String jwtKey;

    @Value("${jwt.refreshExpirationDateInMs}")
    private int refreshExpirationDateInMs;

    public String doGenerateToken(String subject) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Claims claims = Jwts.claims().setSubject(subject);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://jwtdemo.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(cal.getTimeInMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS256, jwtKey).compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return claims.getSubject();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final Claims claims = getAllClaimsFromToken(token);
        LOG.info("claims.getExpiration(): " + claims.getExpiration());
        return (
                claims.getSubject().equals(userDetails.getUsername())
                        && !claims.getExpiration().before(new Date()));
    }
}