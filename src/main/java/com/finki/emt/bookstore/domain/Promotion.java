package com.finki.emt.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "promotions")
public class Promotion implements Serializable {

    @Id
    @Column(name = "book_id")
    private long bookId;

    @Min(1)
    @Column(name = "new_price", nullable = false, precision = 2, scale = 2)
    private double newPrice;

    @NotNull
    @Column(nullable = false)
    private ZonedDateTime start;

    @NotNull
    @Column(nullable = false)
    private ZonedDateTime end;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    public Promotion() {
    }

    public Promotion(long bookId, double newPrice, ZonedDateTime start, ZonedDateTime end) {
        this.bookId = bookId;
        this.newPrice = newPrice;
        this.start = start;
        this.end = end;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.bookId == ((Promotion) obj).getBookId();
    }

    @Override
    public String toString() {
        return String.format("Promotion{bookId=%d, newPrice=%.2f, start=%s, end=%s}",
                             bookId, newPrice, start, end);
    }
}
