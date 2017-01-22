package com.finki.emt.bookstore.web.rest.response;

import com.finki.emt.bookstore.domain.User;

import java.io.Serializable;

public class MinifiedUserResponse implements Serializable {

    private String name;

    private String slug;

    public MinifiedUserResponse() {
    }

    public MinifiedUserResponse(User user) {
        this.name = user.getName();
        this.slug = user.getSlug();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
