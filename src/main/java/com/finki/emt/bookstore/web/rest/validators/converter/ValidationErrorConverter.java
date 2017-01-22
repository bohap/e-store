package com.finki.emt.bookstore.web.rest.validators.converter;

import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationErrorConverter {

    public static Map<String, List<Error>> groupErrors(List<FieldError> errors) {
        return errors.stream()
                .collect(Collectors.groupingBy(FieldError::getField,
                        Collectors.mapping(
                                Error::fromFieldError,
                                Collectors.toList())
                ));
    }

    public static class Error implements Serializable {

        private String code;

        private String message;

        public Error() {
        }

        Error(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        static Error fromFieldError(FieldError error) {
            return new Error(error.getCode(), error.getDefaultMessage());
        }
    }
}
