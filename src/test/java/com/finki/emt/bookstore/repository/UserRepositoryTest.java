package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.domain.Book;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.finki.emt.bookstore.repository.util.ModelUtil.mockBook;
import static com.finki.emt.bookstore.repository.util.ModelUtil.mockUser;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.exparity.hamcrest.date.ZonedDateTimeMatchers.within;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Inject
    private UserRepository repository;

    @Inject
    private ApplicationContext context;

    private RepositoryUtil util;

    private final ZonedDateTime now = ZonedDateTime.now();
    private final Authority authority1 = new Authority("authority 1");
    private final Authority authority2 = new Authority("authority 2");
    private final Authority authority3 = new Authority("authority 3");

    private final long NUMBER_OF_USERS = 3;
    private final long user1Id = 1;
    private final User user1 = mockUser(user1Id, now);
    private final long user2Id = 2;
    private final User user2 = mockUser(user2Id, now);
    private final long user3Id = 3;
    private final User user3 = mockUser(user3Id, now);
    private final long unExistingUserId = 4;
    private final User unExistingUser = mockUser(unExistingUserId, now);

    @Before
    public void setup() {
        user1.setAuthorities(Sets.newLinkedHashSet(authority1, authority3));
        user2.setAuthorities(Collections.singleton(authority2));
        user3.setAuthorities(Sets.newLinkedHashSet(authority1, authority3));

        this.seedTables();
    }

    @After
    public void shutdown() {
        this.clearTables();
        util.close();
    }

    private void seedTables() {
        util = new RepositoryUtil(context);
        util.open();
        util.storeAuthorities(authority1, authority2, authority3);
        util.storeUsers(user1, user2, user3);
    }

    private void clearTables() {
        util.clearTable(User.class, "authorities");
        util.clearTable(Authority.class);
        util.clearTableAndResetId(User.class);
    }

    private void assertUser(User expected, User actual) {
        assertThat(expected.getId(), equalTo(actual.getId()));
        assertThat(expected.getName(), equalTo(actual.getName()));
        assertThat(expected.getSlug(), equalTo(actual.getSlug()));
        assertThat(expected.getEmail(), equalTo(actual.getEmail()));
        assertThat(expected.getPassword(), equalTo(actual.getPassword()));
        assertThat(expected.getCreatedAt(), within(1, SECONDS, actual.getCreatedAt()));
        assertThat(expected.getUpdatedAt(), within(1, SECONDS, actual.getUpdatedAt()));
        assertTrue(CollectionUtil.equals(expected.getAuthorities(), actual.getAuthorities()));

    }

    @Test
    public void testFindAll() {
        List<User> all = repository.findAll();
        assertThat(all, is(notNullValue()));
        assertThat(all.size(), equalTo((int) NUMBER_OF_USERS));
        AtomicInteger count = new AtomicInteger(0);
        all.forEach(u -> {
            if (u.equals(user1)) {
                assertUser(user1, u);
            } else if (u.equals(user2)) {
                assertUser(user2, u);
            } else if (u.equals(user3)) {
                assertUser(user3, u);
            }
            count.incrementAndGet();
        });
        assertThat((int) NUMBER_OF_USERS, equalTo(count.get()));
    }

    @Test
    public void testCount() {
        assertThat(repository.count(), equalTo(NUMBER_OF_USERS));
    }

    @Test
    public void testFindById() {
        User user = repository.findById(user1Id).orElse(null);
        assertUser(user1, user);
    }

    @Test
    public void testFindByIdOnUnExistingId() {
        User user = repository.findById(unExistingUserId).orElse(null);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void testFindBySlug() {
        User user = repository.findBySlug(user2.getSlug()).orElse(null);
        assertUser(user2, user);
    }

    @Test
    public void testFindBySlugOnUnExistingSlug() {
        User user = repository.findBySlug(unExistingUser.getSlug()).orElse(null);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void testFindByEmail() {
        User user = repository.findByEmail(user3.getEmail()).orElseGet(null);
        assertUser(user3, user);
    }

    @Test
    public void testFindByEmailOnUnExistingUser() {
        User user = repository.findByEmail(unExistingUser.getEmail()).orElse(null);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void testStore() {
        User saved = repository.save(unExistingUser);
        assertThat(repository.count(), equalTo(NUMBER_OF_USERS + 1));
        assertUser(unExistingUser, saved);
        assertUser(unExistingUser, repository.findOne(unExistingUserId));
    }

    @Test
    public void testUpdate() {
        user2.setSlug("new slug");
        user3.setEmail("updated@test.com");
        User updated = repository.save(user2);
        assertThat(repository.count(), equalTo(NUMBER_OF_USERS));
        assertUser(user2, updated);
        assertUser(user2, repository.findBySlug(user2.getSlug()).orElse(null));
    }

    @Test
    public void testDelete() {
        repository.delete(user3Id);
        assertThat(repository.count(), equalTo(NUMBER_OF_USERS - 1));
        assertThat(repository.findOne(user3Id), is(nullValue()));
    }

    @Test
    public void testDeleteWithModelInstance() {
        repository.delete(user2);
        assertThat(repository.count(), equalTo(NUMBER_OF_USERS - 1));
        assertThat(repository.findOne(user2Id), is(nullValue()));
    }

    private List<User> getAuthorityUsers(Authority authority) {
        return Stream.of(user1, user2, user3)
                .filter(u -> u.getAuthorities() != null && u.getAuthorities().contains(authority))
                .collect(Collectors.toList());
    }

    @Test
    public void testFindUserWIthAuthority() {
        List<User> users = getAuthorityUsers(authority1);
        List<User> result = repository.findByAuthoritiesName(authority1.getName());
        assertThat(result.size(), equalTo(users.size()));
        assertTrue(CollectionUtil.equals(users, result));
    }

    @Test
    public void testCountUsersWithAuthority() {
        List<User> users = getAuthorityUsers(authority2);
        String authorityName = authority2.getName();
        assertThat(repository.countByAuthoritiesName(authorityName), equalTo((long) users.size()));
    }

    @Test
    public void testUserHaveAAuthority() {
        final User user = unExistingUser;
        user.setAuthorities(Sets.newLinkedHashSet(authority1, authority3));
        repository.save(user);
        User searched = repository
                .findByIdAndAuthoritiesName(user.getId(), authority1.getName())
                .orElse(null);
        assertUser(user, searched);
    }

    @Test
    public void testUserHaveNotAAuthority() {
        final User user = unExistingUser;
        user.setAuthorities(Sets.newLinkedHashSet(authority1, authority3));
        repository.save(user);
        User searched = repository
                .findByIdAndAuthoritiesName(user.getId(), authority2.getName())
                .orElse(null);
        assertThat(searched, is(nullValue()));
    }

    @Test
    public void testUserHaveAAuthorityOnUnExistingUser() {
        User searched = repository
                .findByIdAndAuthoritiesName(unExistingUserId, "test")
                .orElse(null);
        assertThat(searched, is(nullValue()));
    }

    @Test
    @Transactional
    public void testAddingBookToUserFavorites() {
        final Book book1 = mockBook(1, now, user1);
        final Book book2 = mockBook(2, now, user1);
        util.storeBooks(book1, book2);
        Set<Book> books = Sets.newLinkedHashSet(book1, book2);

        User user = unExistingUser;
        user.setFavorites(books);
        User saved = repository.save(user);
        assertTrue(CollectionUtil.equals(user.getFavorites(), saved.getFavorites()));
        User searched = repository.findOne(unExistingUserId);
        assertTrue(CollectionUtil.equals(user.getFavorites(), searched.getFavorites()));

        // Clear the tables
        util.clearTable(Book.class, "favorites");
        util.clearTable(Book.class);
    }

    @Test
    public void testFindLastUser() {
        repository.save(unExistingUser);
        User user = repository.findFirstByOrderByIdDesc().orElse(null);
        assertUser(unExistingUser, user);
    }

    @Test
    public void testFindLastUserOnEmptyDatabase() {
        this.clearTables();
        assertThat(repository.count(), equalTo(0L));
        User user = repository.findFirstByOrderByIdDesc().orElse(null);
        assertThat(user, is(nullValue()));
    }
}
