package com.finki.emt.bookstore.web.rest.vm;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

public class BookVM implements Serializable {

    @NotNull
    private String name;

    @NotNull
    @Size(min = 20, max = 300)
    private String shortDescription;

    @NotNull
    @Size(max = 10000)
    private String body;

    @Min(1)
    private double price;

    private String publisher;

    private String author;

    private int year;

    private int pages;

    @NotNull
    private Set<Category> categories;

    public BookVM() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Book createBook() {
        Book book = new Book(0, name, null, shortDescription, body, null,
                null, price, null, publisher, author, year, pages);
        book.setCategories(categories);
        return book;
    }
}
