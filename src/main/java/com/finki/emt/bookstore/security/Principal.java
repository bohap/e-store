package com.finki.emt.bookstore.security;

import com.finki.emt.bookstore.domain.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class Principal extends org.springframework.security.core.userdetails.User {

    private User user;

    public Principal(User user,
                     Collection<? extends GrantedAuthority> authorities) {
        super(user.getEmail(), user.getPassword(), authorities);
        this.user = user;
    }

    public long getId() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
