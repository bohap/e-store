package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Promotion;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.util.ModelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class PromotionRepositoryTest {

    @Inject
    private PromotionRepository repository;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private UserRepository userRepository;

    @Test
    public void testStore() {
        ZonedDateTime now = ZonedDateTime.now();
        User user = ModelUtil.mockUser(1, now);
        Book book = ModelUtil.mockBook(1, now, user);
        Promotion promotion = new Promotion(book, 123, now, now);
        book.setPromotion(promotion);
        userRepository.save(user);
        bookRepository.save(book);
//        repository.save(promotion);
        Promotion saved = repository.findOne(book.getId());
        assertThat(promotion, equalTo(saved));
    }

    @Test
    public void testDelete() {
        ZonedDateTime now = ZonedDateTime.now();
        User user = ModelUtil.mockUser(1, now);
        Book book = ModelUtil.mockBook(1, now, user);
        Promotion promotion = new Promotion(book, 123, now, now);
        book.setPromotion(promotion);

        userRepository.save(user);
        bookRepository.save(book);

        Promotion saved = repository.findOne(book.getId());
        assertThat(promotion, equalTo(saved));

        book.setPromotion(null);
        bookRepository.save(book);
        assertThat(repository.findOne(book.getId()), is(nullValue()));
    }
}
