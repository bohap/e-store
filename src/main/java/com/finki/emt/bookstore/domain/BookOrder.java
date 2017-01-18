package com.finki.emt.bookstore.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "orders_books")
@AssociationOverrides({
        @AssociationOverride(name = "pk.book",
                joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")}),
        @AssociationOverride(name = "pk.order",
                joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
})
public class BookOrder {

    @EmbeddedId
    private BookOrderId pk;

    @NotNull
    @Min(1)
    @Column(nullable = false, precision = 2, scale = 2)
    private double price;

    public BookOrder() {
    }

    public BookOrder(BookOrderId pk, double price) {
        this.pk = pk;
        this.price = price;
    }

    public BookOrder(Book book, Order order, double price) {
        this.pk = new BookOrderId(book, order);
        this.price = price;
    }

    public BookOrderId getPk() {
        return pk;
    }

    public void setPk(BookOrderId pk) {
        this.pk = pk;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.pk != null && this.pk.equals(((BookOrder) obj).getPk());
    }

    @Override
    public String toString() {
        return String.format("BookOrder={book=%s, order=%s, price=%.2f}",
                             pk.getBook(), pk.getOrder(), price);
    }
}
