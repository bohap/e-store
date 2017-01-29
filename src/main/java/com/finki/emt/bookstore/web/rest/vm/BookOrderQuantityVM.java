package com.finki.emt.bookstore.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finki.emt.bookstore.domain.Book;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class BookOrderQuantityVM implements Serializable {

    @NotNull
    private String slug;

    @Min(1)
    private int quantity;

    public BookOrderQuantityVM() {
    }

    public BookOrderQuantityVM(String slug, int quantity) {
        this.slug = slug;
        this.quantity = quantity;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
