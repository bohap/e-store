package com.finki.emt.bookstore.web.rest.vm;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class LoginVM implements Serializable {

    @NotNull
    @Email
    private String username;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    public LoginVM() {
    }

    public LoginVM(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("LoginVM{username='%s', password='%s'}", username, password);
    }
}
