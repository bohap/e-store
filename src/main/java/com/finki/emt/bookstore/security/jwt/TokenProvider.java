package com.finki.emt.bookstore.security.jwt;

import com.finki.emt.bookstore.config.BookStoreProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);

    @Inject
    private BookStoreProperties properties;

    private static final String AUTHORITIES_KEY = "auth";

    private String secretKey;

    private long tokenValidInMilliseconds;

    private long tokenRefreshableInMilliseconds;

    @PostConstruct
    public void init() {
        this.secretKey = properties.getJwt().getSecret();
        this.tokenValidInMilliseconds =
                1000 * 60 * properties.getJwt().getTokenValidInMinutes();
        this.tokenRefreshableInMilliseconds =
                1000 * 60 * properties.getJwt().getTokenRefreshableInMinutes();
    }

    /**
     * Create a new JWT token for the authentication.
     *
     * @param authentication authenticated user
     * @return jwt token
     */
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();
        Date validity = new Date(now + this.tokenValidInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Get the token claims.
     *
     * @param token the jwt token
     * @return token claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Get the authentication from the token.
     *
     * @param token the jwt token content
     * @return user authentication
     */
    public Authentication getAuthentication(String token) {
        Claims claims = this.getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * Check if the token is valid.
     *
     * @param token the token that will be checked
     * @return whatever the token is valid
     */
    public boolean validateToken(String token) {
        try {
            this.getClaims(token);
            return true;
        } catch (SignatureException exp) {
            log.info("Invalid token exception {}", exp.getMessage());
            return false;
        }
    }

    /**
     * Try to refresh the token
     *
     * @param token the old token
     * @return refreshed token
     */
    public String refreshToken(String token) throws ExpiredJwtException {
        return null;
    }
}
