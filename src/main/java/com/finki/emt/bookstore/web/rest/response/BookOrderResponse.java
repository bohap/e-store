package com.finki.emt.bookstore.web.rest.response;

import com.finki.emt.bookstore.domain.BookOrder;

import java.io.Serializable;

public class BookOrderResponse implements Serializable {

    private MinifiedBookResponse book;

    private double price;

    private int quantity;

    public BookOrderResponse() {
    }

    public BookOrderResponse(BookOrder bookOrder) {
        this.book = new MinifiedBookResponse(bookOrder.getPk().getBook());
        this.price = bookOrder.getPrice();
        this.quantity = bookOrder.getQuantity();
    }

    public MinifiedBookResponse getBook() {
        return book;
    }

    public void setBook(MinifiedBookResponse book) {
        this.book = book;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
