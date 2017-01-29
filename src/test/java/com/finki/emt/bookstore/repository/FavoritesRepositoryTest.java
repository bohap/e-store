package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.util.ModelUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class FavoritesRepositoryTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private CategoryRepository categoryRepository;

    private final ZonedDateTime now = ZonedDateTime.now();

    @Test
    public void testStore() {
        User admin = ModelUtil.mockUser(1, now);
        User user = ModelUtil.mockUser(2, now);
        Book book = ModelUtil.mockBook(1, now, admin);
        book.setFavorites(Collections.singleton(user));
//        user.setFavorites(Collections.singleton(book));

        userRepository.save(admin);
        userRepository.save(user);
        bookRepository.save(book);
    }

    @Test
    public void testGet() {
        User admin = ModelUtil.mockUser(1, now);
        User user = ModelUtil.mockUser(2, now);
        Book book = ModelUtil.mockBook(1, now, admin);
        book.setFavorites(Collections.singleton(user));

        userRepository.save(admin);
        userRepository.save(user);
        bookRepository.save(book);

        Book find = bookRepository.findBySlugAndFavoritesId(book.getSlug(), user.getId()).orElse(null);
        assertThat(find, equalTo(book));
    }
}
