package com.finki.emt.bookstore.web.rest.errors;

public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(String message) {
        super(message);
    }
}
