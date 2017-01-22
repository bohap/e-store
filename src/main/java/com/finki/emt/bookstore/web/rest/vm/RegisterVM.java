package com.finki.emt.bookstore.web.rest.vm;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegisterVM implements Serializable {

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;

    @NotNull
    @Size(min = 6, max = 30)
    private String passwordConfirmation;

    public RegisterVM() {
    }

    public RegisterVM(String name, String email, String password, String passwordConfirmation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public String toString() {
        return String.format("RegisterVm{name='%s', email='%s', password='%s', " +
                "passwordConfirmation='%s'}", name, email, password, passwordConfirmation);
    }
}
