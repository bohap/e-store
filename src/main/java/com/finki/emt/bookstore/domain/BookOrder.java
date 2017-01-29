package com.finki.emt.bookstore.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;

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

    @Min(1)
    @Column(nullable = false, precision = 2, scale = 2)
    private double price;

    @Min(1)
    @Column(nullable = false)
    private int quantity;

    public BookOrder() {
    }

    public BookOrder(BookOrderId pk, double price, int quantity) {
        this.pk = pk;
        this.price = price;
        this.quantity = quantity;
    }

    public BookOrder(Book book, Order order, double price, int quantity) {
        this.pk = new BookOrderId(book, order);
        this.price = price;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
