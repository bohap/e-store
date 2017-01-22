package com.finki.emt.bookstore.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTFiler extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(JWTFiler.class);

    private TokenProvider tokenProvider;

    public JWTFiler(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                            throws IOException, ServletException {
        try {
            log.debug("Checking if request contains the token");
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String jwt = resolveToken(httpServletRequest);
            if (StringUtils.hasText(jwt)) {
                log.debug("Request contains JWT token");
                if (tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException exp) {
            log.info("Security exception for user {} - {}",
                    exp.getClaims().getSubject(), exp.getMessage());
            throw exp;
        }
    }

    /**
     * Get the token content from the request.
     *
     * @param request the http request
     * @return token content, or null if the token can't be find
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        String jwt = request.getParameter(JWTConfigurer.AUTHORIZATION_TOKEN);
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        log.debug("Request don't contains token");
        return null;
    }
}
