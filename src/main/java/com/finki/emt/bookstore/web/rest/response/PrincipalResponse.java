package com.finki.emt.bookstore.web.rest.response;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.domain.User;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class PrincipalResponse implements Serializable {

    private String slug;

    private String name;

    private Set<String> authorities;

    public PrincipalResponse() {
    }

    public PrincipalResponse(User user) {
        this.slug = user.getSlug();
        this.name = user.getName();
        this.authorities = user.getAuthorities().stream()
                .map(Authority::getName)
                .collect(Collectors.toSet());
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
}
