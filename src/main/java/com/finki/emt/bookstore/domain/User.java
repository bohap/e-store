package com.finki.emt.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseModel implements Serializable {

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String slug;

    @Email
    @NotNull
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 6, max = 255)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Basket basket;

    @JsonIgnore
    @ManyToMany(mappedBy = "favorites")
    private Set<Book> favorites;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "admin")
    private Set<Book> createdBooks;

    public User() {
    }

    public User(long id, String name, String slug, String email, String password,
                ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        super(id);
        this.name = name;
        this.slug = slug;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String name, String email, String password) {
        this(0, name, null, email, password, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public Set<Book> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<Book> favorites) {
        this.favorites = favorites;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Book> getCreatedBooks() {
        return createdBooks;
    }

    public void setCreatedBooks(Set<Book> createdBooks) {
        this.createdBooks = createdBooks;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.getId() == ((User) obj).getId();
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', slug='%s', email='%s'}",
                             getId(), name, slug, email);
    }
}
