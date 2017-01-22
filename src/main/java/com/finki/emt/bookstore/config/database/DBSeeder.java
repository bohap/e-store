package com.finki.emt.bookstore.config.database;

import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class DBSeeder {

    @Inject
    private AuthoritySeeder authoritySeeder;

    @Inject
    private CategorySeeder categorySeeder;

    @Inject
    private UserSeeder userSeeder;

    @Inject
    private BookSeeder bookSeeder;

    @Inject
    private PromotionSeeder promotionSeeder;

    @Inject
    private OrderSeeder orderSeeder;

    @Inject
    private FavoriteBooksSeeder favoriteBooksSeeder;

    @Inject
    private BasketSeeder basketSeeder;

    public void seed() {
        authoritySeeder.seed();
        categorySeeder.seed();
        userSeeder.seed();
        bookSeeder.seed();
        promotionSeeder.seed();
        orderSeeder.seed();
        favoriteBooksSeeder.seed();
        basketSeeder.seed();
    }
}
