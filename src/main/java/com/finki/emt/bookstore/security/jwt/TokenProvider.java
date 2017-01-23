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
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

        Date now = new Date();
        long nowMills = now.getTime();
        Date validity = new Date(nowMills + this.tokenValidInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, this.secretKey)
                .setIssuedAt(now)
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
     * Create a new authentication for the given claims.
     *
     * @param claims   the token claims
     * @return  spring security authentication
     */
    private Authentication createAuthentication(Claims claims) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    /**
     * Get the authentication from the token.
     *
     * @param token the jwt token content
     * @return user authentication
     */
    public Authentication getAuthentication(String token) {
        Claims claims = this.getClaims(token);
        return this.createAuthentication(claims);
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
     * Checks if the token can be refreshed.
     *
     * @param issuedAt the data the token was issued at
     * @return  whatever the token is refreshable
     */
    public boolean isTokenRefreshable(Date issuedAt) {
        long nowMills = new Date().getTime();
        long issuedAtMills = issuedAt.getTime();
        return nowMills < issuedAtMills + tokenRefreshableInMilliseconds;
    }

    /**
     * Try to refresh the token
     *
     * @param token the old token
     * @return refreshed token
     */
    public String refreshToken(String token) throws ExpiredJwtException {
        log.debug("Request to refresh token - {}", token);
        Claims claims;
        try {
            claims = this.getClaims(token);
        } catch (ExpiredJwtException exp) {
            claims = exp.getClaims();
        }
        if (!isTokenRefreshable(claims.getIssuedAt())) {
            throw new ExpiredJwtException(null, claims, "Token is expired and can't be refreshed");
        }

        Authentication authentication = this.createAuthentication(claims);
        return this.createToken(authentication);
    }

    /**
     * Get the token from the request.
     *
     * @param request   the http request
     * @return  the token, or null if the request don't contains the token
     */
    public String resolveTokenFromRequest(HttpServletRequest request) {
        log.debug("Request to resolve token from the http request");
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        final String carrier = JWTConfigurer.AUTHORIZATION_CARRIER;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(carrier)) {
            log.debug("Request contains JWT token");
            return bearerToken.substring(carrier.length(), bearerToken.length());
        }
        String jwt = request.getParameter(JWTConfigurer.AUTHORIZATION_TOKEN);
        if (StringUtils.hasText(jwt)) {
            log.debug("Request contains JWT token");
            return jwt;
        }
        log.debug("Request don't contains token");
        return null;
    }
}
