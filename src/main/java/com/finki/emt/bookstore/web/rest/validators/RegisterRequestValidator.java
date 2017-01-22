package com.finki.emt.bookstore.web.rest.validators;

import com.finki.emt.bookstore.service.UserService;
import com.finki.emt.bookstore.web.rest.vm.RegisterVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterRequestValidator implements Validator {

    private final Logger log = LoggerFactory.getLogger(RegisterRequestValidator.class);

    @Autowired
    private UserService service;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RegisterVM.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.debug("Register request validating - {}", target);
        RegisterVM request = (RegisterVM) target;
        this.checkUniqueEmail(request.getEmail(), errors);
        this.checkPasswordMath(request.getPassword(), request.getPasswordConfirmation(), errors);
    }

    private void checkUniqueEmail(String email, Errors errors) {
        service.findByEmail(email).ifPresent(u -> {
            log.debug("Register request with existing email - {}", email);
            errors.rejectValue("email", "notUnique",
                    String.format("user with email %s already exists", email));
        });
    }

    private void checkPasswordMath(String password, String passwordConfirmation, Errors errors) {
        if (password != null && passwordConfirmation != null &&
                !password.equals(passwordConfirmation)) {
            errors.rejectValue("passwordConfirmation", "notMatch",
                    "Password confirmation don't match");
        }
    }
}
