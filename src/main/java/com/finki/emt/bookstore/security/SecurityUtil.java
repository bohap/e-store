package com.finki.emt.bookstore.security;

import com.finki.emt.bookstore.domain.Authority;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class SecurityUtil {

    @Inject
    private UserDetailsService userDetailsService;

    public Authentication authenticate(Authentication authentication) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                                                        authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        return token;
    }

    public Authentication authenticate(UserDetails userDetails,
                                       Collection<? extends GrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
        return token;
    }

    public static Collection<? extends GrantedAuthority> createAuthorities(Collection<Authority> authorities) {
        return authorities.stream()
                .map(a -> new SimpleGrantedAuthority(a.getName()))
                .collect(Collectors.toList());
    }

    public Principal getAuthentication() {
        return (Principal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
