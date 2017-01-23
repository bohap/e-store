package com.finki.emt.bookstore.security;

import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Optional;

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
            Collection<? extends GrantedAuthority> authorities =
                    SecurityUtil.createAuthorities(u.getAuthorities());

            return new Principal(u, authorities);
        }).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with email %s was not found", username)));
    }
}
