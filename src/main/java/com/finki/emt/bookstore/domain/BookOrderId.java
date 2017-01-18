package com.finki.emt.bookstore.domain;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class BookOrderId implements Serializable {

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Order order;

    public BookOrderId() {
    }

    public BookOrderId(Book book, Order order) {
        this.book = book;
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }
        BookOrderId id = (BookOrderId) obj;
        return this.book != null && this.book.equals(id.getBook()) &&
                this.order != null &&  this.order != id.getOrder();
    }

    @Override
    public int hashCode() {
        int bookId = book != null ? (int) book.getId() : 0;
        int orderId = order != null ? (int) order.getId() : 0;
        return bookId + orderId;
    }
}
