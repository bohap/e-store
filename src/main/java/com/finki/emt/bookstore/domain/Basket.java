package com.finki.emt.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "baskets")
public class Basket implements Serializable {

    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "baskets_books",
            joinColumns = {@JoinColumn(name = "basket_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")})
    private Set<Book> books;

    public Basket() {
    }

    public Basket(long userId, ZonedDateTime updatedAt) {
        this.userId = userId;
        this.updatedAt = updatedAt;
    }

    public Basket(User user, ZonedDateTime updatedAt) {
        this.user = user;
        this.updatedAt = updatedAt;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.userId = user.getId();
        }
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.userId == ((Basket) obj).getUserId();
    }

    @Override
    public String toString() {
        return String.format("Basket{userId=%d, updatedAt='%s'}", userId, updatedAt);
    }
}
