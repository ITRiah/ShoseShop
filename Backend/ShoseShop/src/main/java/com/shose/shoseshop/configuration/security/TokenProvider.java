package com.shose.shoseshop.configuration.security;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${security.jwt.secret}")
    String tokenSecretKey;
    @Value("${security.jwt.token-validity-in-seconds}")
    Long tokenValidityInSeconds;

    private static final String AUTHORITIES_KEY = "auth";
    private final RedisTemplate<String, String> redisTemplate;

    long tokenValidityInMilliseconds;

    @PostConstruct
    protected void init(){
        this.tokenSecretKey = Base64.getEncoder().encodeToString(tokenSecretKey.getBytes());
        this.tokenValidityInMilliseconds = 1000 * tokenValidityInSeconds;
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put(AUTHORITIES_KEY, authorities);
        //todo put another claims

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS512, tokenSecretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenSecretKey)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .toList();
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        if (redisTemplate.hasKey("blacklist_token:" + token).equals(Boolean.TRUE)) {
            log.warn("Token is blacklisted: {}", token);
            return false;
        }

        try {
            Jwts.parser().setSigningKey(tokenSecretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new RuntimeException("Token expired");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid token");
        } catch (SignatureException e) {
            log.error("JWT signature validation failed: {}", e.getMessage());
            throw new RuntimeException("Invalid signature");
        } catch (IllegalArgumentException e) {
            log.error("Token validation error: {}", e.getMessage());
            throw new RuntimeException("Token is empty or null");
        }
    }

    public void blacklistToken(String token, long ttlSeconds) {
        redisTemplate.opsForValue().set(
                "blacklist_token:" + token,
                token,
                ttlSeconds,
                TimeUnit.SECONDS
        );
        log.info("Token blacklisted: {}", token);
    }

    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }
}
