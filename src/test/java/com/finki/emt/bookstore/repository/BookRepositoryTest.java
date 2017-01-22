package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.repository.util.RepositoryUtil;
import com.finki.emt.bookstore.util.CollectionUtil;
import org.assertj.core.util.Sets;
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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.finki.emt.bookstore.util.FileUtil.readBytes;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.exparity.hamcrest.date.ZonedDateTimeMatchers.within;
import static org.hamcrest.Matchers.*;
import static org.hibernate.internal.util.StringHelper.repeat;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Inject
    private BookRepository repository;

    @Inject
    private ApplicationContext context;

    private RepositoryUtil util;
    private final ZonedDateTime now = ZonedDateTime.now();

    private User admin1 = new User(1, "admin 1", "admin-1", "admin-1@admin.com", "password", now, now);
    private User admin2 = new User(2, "admin 2", "admin-2", "admin-2@admin.com", "password", now, now);
    private final Category category1 = new Category(1, "category 1");
    private final Category category2 = new Category(2, "category 2");
    private final Category category3 = new Category(3, "category 3");

    private final long NUMBER_OF_BOOKS = 3;
    private final long book1Id = 1;
    private final Book book1 =
            new Book(book1Id, "Book 1", "book-1", repeat("description 1", 10), repeat("body 1", 50),
                     now, now, 100, readBytes("img/book.png"), "Publisher 1", "Author 1", 1234, 1234);
    private final long book2Id = 2;
    private final Book book2 =
            new Book(book2Id, "Book 2", "book-2", repeat("description 2", 10), repeat("body 2", 50),
                     now, now, 1151, readBytes("img/book.png"), "Publisher 2", "Author 2", 1234, 1234);
    private final long book3Id = 3;
    private final Book book3 =
            new Book(book3Id, "Book 3", "book-3", repeat("description 3", 10), repeat("body 3", 50),
                     now, now, 84, readBytes("img/book.png"), "Publisher 3", null, 1234, 1234);
    private final long unExistingBookId = 4;
    private final Book unExistingBook =
            new Book(unExistingBookId, "Un Existing Book", "un-existing-book", repeat("un-existing", 10),
                     repeat("body", 20), now, now, 124, readBytes("img/book.png"), null, null, 0, 0);

    @Before
    public void setup() {
        book1.setAdmin(admin1);
        book1.setCategories(Sets.newLinkedHashSet(category1, category2, category3));
        book2.setAdmin(admin2);
        book2.setCategories(Collections.singleton(category2));
        book3.setAdmin(admin1);
        book3.setCategories(Sets.newLinkedHashSet(category1, category3));
        unExistingBook.setAdmin(admin2);
        unExistingBook.setCategories(Collections.singleton(category1));

        util = new RepositoryUtil(context);
        util.open();
        util.storeUsers(admin1, admin2);
        util.storeCategories(category1, category2, category3);
        util.storeBooks(book1, book2, book3);
    }

    @After
    public void shutdown() {
        util.clearTable(Book.class, "categories");
        util.clearTableAndResetId(Category.class);
        util.clearTableAndResetId(Book.class);
        util.clearTableAndResetId(User.class);
        util.close();
    }

    /**
     * Assert that the books are the same
     */
    private void assertBook(Book expected, Book actual) {
        assertThat(actual, is(notNullValue()));
        assertThat(expected.getId(), equalTo(actual.getId()));
        assertThat(expected.getName(), equalTo(actual.getName()));
        assertThat(expected.getSlug(), equalTo(actual.getSlug()));
        assertThat(expected.getShortDescription(), equalTo(actual.getShortDescription()));
        assertThat(expected.getBody(), equalTo(actual.getBody()));
        assertThat(expected.getCreatedAt(), within(1, SECONDS, actual.getCreatedAt()));
        assertThat(expected.getUpdatedAt(), within(1, SECONDS, actual.getUpdatedAt()));
        assertThat(expected.getImage(), equalTo(actual.getImage()));
        assertThat(expected.getPrice(), equalTo(actual.getPrice()));
        assertThat(expected.getPublisher(), equalTo(actual.getPublisher()));
        assertThat(expected.getAuthor(), equalTo(actual.getAuthor()));
        assertThat(expected.getYear(), equalTo(actual.getYear()));
        assertThat(expected.getPages(), equalTo(actual.getPages()));
        assertThat(expected.getAdmin(), equalTo(actual.getAdmin()));
        assertTrue(CollectionUtil.equals(expected.getCategories(), actual.getCategories()));
    }

    @Test
    public void testFindAll() {
        List<Book> all = repository.findAll();
        assertThat(all, is(notNullValue()));
        assertThat(all.size(), equalTo((int) NUMBER_OF_BOOKS));
        AtomicInteger count = new AtomicInteger();
        all.forEach(b -> {
            if (b.equals(book1)) {
                assertBook(book1, b);
            } else if (b.equals(book2)) {
                assertBook(book2, b);
            } else if (b.equals(book3)) {
                assertBook(book3, b);
            }
            count.incrementAndGet();
        });
        assertThat(count.get(), equalTo((int) NUMBER_OF_BOOKS));
    }

    @Test
    public void testCount() {
        assertThat(repository.count(), equalTo(NUMBER_OF_BOOKS));
    }

    @Test
    public void tesFinaById() {
        Book book = repository.findOne(book1Id);
        assertBook(book1, book);
    }

    @Test
    public void testFindBySlug() {
        Book book = repository.findBySlug(book2.getSlug()).orElse(null);
        assertBook(book2, book);
    }

    @Test
    public void testStore() {
        unExistingBook.setCategories(Sets.newLinkedHashSet(category1, category3));
        Book saved = repository.save(unExistingBook);
        assertThat(repository.count(), equalTo(NUMBER_OF_BOOKS + 1));
        assertBook(unExistingBook, saved);
        assertBook(unExistingBook, repository.findOne(unExistingBookId));
    }

    @Test
    public void testUpdate() {
        book3.setSlug("new-slug");
        book3.setAuthor("New Author");
        Book updated = repository.save(book3);
        assertThat(repository.count(), equalTo(NUMBER_OF_BOOKS));
        assertBook(book3, updated);
        assertBook(book3, repository.findOne(book3.getId()));
    }

    @Test
    public void testDeleteWithId() {
        repository.delete(book2Id);
        assertThat(repository.count(), equalTo(NUMBER_OF_BOOKS - 1));
        assertThat(repository.findOne(book2Id), is(nullValue()));
    }

    @Test
    public void testDeleteWIthEntity() {
        repository.delete(book1);
        assertThat(repository.count(), equalTo(NUMBER_OF_BOOKS - 1));
        assertThat(repository.findOne(book1Id), is(nullValue()));
    }

    @Test
    @Transactional
    public void testFavorites() {

    }
}
