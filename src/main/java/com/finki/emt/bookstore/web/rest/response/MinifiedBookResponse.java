package com.finki.emt.bookstore.web.rest.response;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.Promotion;

import java.io.Serializable;
import java.util.Set;

public class MinifiedBookResponse implements Serializable {

    private long id;

    private String slug;

    private String name;

    private String shortDescription;

    private double price;

    private Set<Category> categories;

    private Promotion promotion;

    // TODO add the ide as a param

    public MinifiedBookResponse() {
    }

    public MinifiedBookResponse(Book book) {
        this.id = book.getId();
        this.slug = book.getSlug();
        this.name = book.getName();
        this.shortDescription = book.getShortDescription();
        this.price = book.getPrice();
        this.categories = book.getCategories();
        this.promotion = book.getPromotion();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return this.name;
    }

    public String setName(String name) {
        return this.name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }
}
