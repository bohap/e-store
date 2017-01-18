package com.finki.emt.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order extends BaseModel implements Serializable {

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Column(name = "finished", nullable = false)
    private boolean finished;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "pk.order")
    private Set<BookOrder> books;

    public Order() {
    }

    public Order(long id, ZonedDateTime createdAt, ZonedDateTime updatedAt, boolean finished) {
        super(id);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.finished = finished;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<BookOrder> getBooks() {
        return books;
    }

    public void setBooks(Set<BookOrder> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.getId() == ((Order) obj).getId();
    }

    @Override
    public String toString() {
        return String.format("Order{id=%s, user=%s, createdAt=%s, updatedAt=%s, finished=%b}",
                             getId(), user, createdAt, updatedAt, finished);
    }
}
