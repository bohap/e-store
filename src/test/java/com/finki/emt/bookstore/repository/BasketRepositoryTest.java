package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.Basket;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Set;

import static com.finki.emt.bookstore.repository.util.ModelUtil.mockBook;
import static com.finki.emt.bookstore.repository.util.ModelUtil.mockUser;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class BasketRepositoryTest {

    @Inject
    private BasketRepository repository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private ApplicationContext context;

    private final ZonedDateTime now = ZonedDateTime.now();
//    private final long NUMBER_OF_USERS = 3;
//    private final long user1Id = 1;
//    private final User user1 = new User(user1Id, "user 1", "user-1", "user-1@test.com", "password", now, now);
//    private final Basket basket1 = new Basket(user1Id, now);
//    private final long user2Id = 2;
//    private final User user2 = new User(user2Id, "user 2", "user-2", "user-2@test.com", "password", now, now);
//    private final Basket basket2 = new Basket(user2Id, now);
//    private final long user3Id = 3;
//    private final User user3 = new User(user3Id, "user 3", "user-3", "user-3@test.com", "password", now, now);
//    private final Basket basket3 = new Basket(user3Id, now);
//    private final long unExistingUserId = 4;
//    private final User unExistingUser = new User(unExistingUserId, "un existing user", "un-existing-user",
//                                                "unexisting-user@test.com", "password", now, now);
//    private Basket unExistingBasket = new Basket(unExistingUserId, now);

    @Before
    public void setup() {
        User admin = mockUser(1, now);
        Book book = mockBook(1, now, admin);
        User user = mockUser(2, now);
        Basket basket = new Basket(user, now);
        basket.setBooks(Collections.singleton(book));
        user.setBasket(basket);

        userRepository.save(admin);
        bookRepository.save(book);
        userRepository.save(user);
    }

    @After
    public void shutdown() {

    }

    @Test
    @Transactional
    public void test() {
        assertThat(repository.count(), equalTo(1L));
        Basket basket = repository.findOne(2L);
        assertThat(basket, is(notNullValue()));
        Set<Book> books = basket.getBooks();
        assertThat(books.size(), equalTo(1));
        books.add(new Book());
        assertThat(books.size(), equalTo(2));
    }
}
