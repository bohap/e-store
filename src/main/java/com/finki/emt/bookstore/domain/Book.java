package com.finki.emt.bookstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finki.emt.bookstore.config.Constants;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "books")
@Indexed
@AnalyzerDef(name = "bookStoreAnalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {@TokenFilterDef(factory = LowerCaseFilterFactory.class)})
@Analyzer(definition = "bookStoreAnalyzer")
public class Book extends BaseModel implements Serializable {

    @NotNull
    @Column(nullable = false)
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES)
    @SortableField
    @Boost(2f)
    private String name;

    @NotNull
    @Column(nullable = false, unique = true)
    private String slug;

    @NotNull
    @Size(min = 20, max = 300)
    @Column(name = "short_description", nullable = false)
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES)
    @Boost(1f)
    private String shortDescription;

    @NotNull
    @Size(max = 10000)
    @Column(nullable = false, length = 10000)
    private String body;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @Min(1)
    @Column(nullable = false, precision = 2, scale = 2)
    private double price;

    @JsonIgnore
    @NotNull
    @Column(nullable = false, length = Constants.BOOK_IMAGE_MAX_SIZE)
    private byte[] image;

    private String publisher;

    private String author;

    private int year;

    private int pages;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_categories",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    @IndexedEmbedded
    private Set<Category> categories;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "book", cascade = CascadeType.ALL,
              orphanRemoval = true)
    private Promotion promotion;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "favorite_books",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> favorites;

    @JsonIgnore
    @ManyToMany(mappedBy = "books", cascade = CascadeType.ALL)
    private Set<Basket> baskets;

    @JsonIgnore
    @OneToMany(mappedBy = "pk.book", cascade = CascadeType.ALL)
    private Set<BookOrder> orders;

    public Book() {
    }

    public Book(long id, String name, String slug, String shortDescription, String body,
                ZonedDateTime createdAt, ZonedDateTime updatedAt, double price, byte[] image,
                String publisher, String author, int year, int pages) {
        super(id);
        this.name = name;
        this.slug = slug;
        this.shortDescription = shortDescription;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.price = price;
        this.image = image;
        this.publisher = publisher;
        this.author = author;
        this.year = year;
        this.image = image;
        this.publisher = publisher;
        this.author = author;
        this.year = year;
        this.pages = pages;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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

    public Set<User> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<User> favorites) {
        this.favorites = favorites;
    }

    public Set<Basket> getBaskets() {
        return baskets;
    }

    public void setBaskets(Set<Basket> baskets) {
        this.baskets = baskets;
    }

    public Set<BookOrder> getOrders() {
        return orders;
    }

    public void setOrders(Set<BookOrder> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.getId() == ((Book) obj).getId();
    }

    @Override
    public String toString() {
        return String.format("Book{id=%d, name='%s', slug='%s'}", getId(), name, slug);
    }
}
