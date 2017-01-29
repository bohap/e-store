package com.finki.emt.bookstore.web.rest.vm;

import com.finki.emt.bookstore.domain.BillingAddress;
import com.finki.emt.bookstore.domain.CreditCard;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class BookOrderVM implements Serializable {

    @NotNull
    private BillingAddress billingAddress;

    @NotNull
    private CreditCard creditCard;

    @Valid
    private List<BookOrderQuantityVM> books;

    public BookOrderVM() {
    }

    public BookOrderVM(BillingAddress billingAddress, CreditCard creditCard,
                       List<BookOrderQuantityVM> books) {
        this.billingAddress = billingAddress;
        this.creditCard = creditCard;
        this.books = books;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<BookOrderQuantityVM> getBooks() {
        return books;
    }

    public void setBooks(List<BookOrderQuantityVM> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return String.format("BookOrderVM{" +
                "billingAddress='%s', " +
                "creditCard='%s', " +
                "books='%s'" +
                "}", billingAddress, creditCard,
                    books.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")));
    }
}
