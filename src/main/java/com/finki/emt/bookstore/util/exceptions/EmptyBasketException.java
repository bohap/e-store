package com.finki.emt.bookstore.util.exceptions;

public class EmptyBasketException extends RuntimeException {

    public EmptyBasketException() {
        super("The basket is empty");
    }
}
