package com.finki.emt.bookstore.config;

import com.finki.emt.bookstore.security.AuthoritiesConstants;
import com.finki.emt.bookstore.security.CustomAccessDeniedHandler;
import com.finki.emt.bookstore.security.Http401UnauthorizedEntryPoint;
import com.finki.emt.bookstore.security.SecurityUtil;
import com.finki.emt.bookstore.security.jwt.JWTConfigurer;
import com.finki.emt.bookstore.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private TokenProvider tokenProvider;

    @Inject
    private SecurityUtil securityUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
                // users routes
                .antMatchers("/api/users/count")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/users/{slug}/**")
                    .authenticated()
                .antMatchers("/api/users/**")
                    .hasAuthority(AuthoritiesConstants.ADMIN)

                // books routes
                .antMatchers("/api/books/count")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers(HttpMethod.POST, "/api/books")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers(HttpMethod.PUT, "/api/books/{slug}")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers(HttpMethod.DELETE, "/api/books/{slug}")
                    .hasAuthority(AuthoritiesConstants.ADMIN)

                // favorite books routes
                .antMatchers("/api/books/{slug}/favorites/**")
                    .hasAuthority(AuthoritiesConstants.USER)

                // basket routes
                .antMatchers("/api/basket/**")
                    .hasAuthority(AuthoritiesConstants.USER)

                // orders routes
                .antMatchers("/api/orders/count")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                .antMatchers("/api/orders/{id}/**")
                    .authenticated()
                .antMatchers("/api/orders/**")
                    .hasAuthority(AuthoritiesConstants.ADMIN)

                // promotion routes
                .antMatchers("/api/books/{slug}/promotion")
                    .hasAuthority(AuthoritiesConstants.ADMIN)

                .antMatchers("/api/**").permitAll()
        .and()
            .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider, securityUtil);
    }
}
