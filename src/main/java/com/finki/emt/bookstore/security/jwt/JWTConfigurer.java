package com.finki.emt.bookstore.security.jwt;

import com.finki.emt.bookstore.security.SecurityUtil;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    static final String AUTHORIZATION_TOKEN = "access_token";

    public static final String AUTHORIZATION_CARRIER = "Bearer ";

    public static final String AUTHORIZATION_REFRESH_ROUTE = "/refresh";

    private TokenProvider tokenProvider;

    private SecurityUtil securityUtil;

    public JWTConfigurer(TokenProvider tokenProvider, SecurityUtil securityUtil) {
        this.tokenProvider = tokenProvider;
        this.securityUtil = securityUtil;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        JWTFiler customFilter = new JWTFiler(tokenProvider, securityUtil);
        builder.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
