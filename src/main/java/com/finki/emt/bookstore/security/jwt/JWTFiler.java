package com.finki.emt.bookstore.security.jwt;

import com.finki.emt.bookstore.security.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFiler extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(JWTFiler.class);

    private TokenProvider tokenProvider;

    private SecurityUtil securityUtil;

    JWTFiler(TokenProvider tokenProvider, SecurityUtil securityUtil) {
        this.tokenProvider = tokenProvider;
        this.securityUtil = securityUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                                                            throws IOException, ServletException {
        log.debug("Checking if request contains the token");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse  =(HttpServletResponse) response;
        try {
            String jwt = tokenProvider.resolveTokenFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt)) {
                if (tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    log.debug("Authenticating user - {}", authentication.getName());
                    securityUtil.authenticate(authentication);
                }
            }
        } catch (ExpiredJwtException exp) {
            log.info("Security exception for user {} - {}",
                    exp.getClaims().getSubject(), exp.getMessage());

            boolean isRefreshRoute = httpServletRequest.getServletPath()
                    .endsWith(JWTConfigurer.AUTHORIZATION_REFRESH_ROUTE);
            boolean isRefreshable = tokenProvider.isTokenRefreshable(exp.getClaims().getIssuedAt());
            if (!isRefreshRoute || !isRefreshable) {
                log.info("Token is expired and can't be refreshed");
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expired");
                return;
            }
        }
        catch (UsernameNotFoundException exp) {
            log.info("Security exception - {}", exp.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token data");
            return;
        }
        chain.doFilter(request, response);
    }
}
