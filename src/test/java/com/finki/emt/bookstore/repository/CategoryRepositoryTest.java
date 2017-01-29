package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.BookStoreApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BookStoreApplication.class)
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Inject
    private CategoryRepository repository;

    @Test
    public void test() {
        repository.groupByBooks();
    }
}
