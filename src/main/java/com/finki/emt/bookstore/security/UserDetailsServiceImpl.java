package com.finki.emt.bookstore.security;

import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Inject
    private UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Authenticating - {}", username);
        Optional<User> user = repository.findByEmail(username);
        return user.map(u -> {
            List<GrantedAuthority> authorities = u.getAuthorities().stream()
                    .map(a -> new SimpleGrantedAuthority(a.getName()))
                    .collect(Collectors.toList());

            return new org.springframework.security.core.userdetails.User(
                    u.getEmail(), u.getPassword(), authorities);
        }).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email %s was not found", username)));
    }
}
