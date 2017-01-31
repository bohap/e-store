package com.finki.emt.bookstore.repository.search.impl;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.repository.search.BookSearchRepository;
import com.finki.emt.bookstore.repository.search.SearchRepository;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.List;

@Repository
public class BookSearchRepositoryImpl implements BookSearchRepository {

    private final String[] fields = {"name", "shortDescription", "categories.name"};

    @Inject
    private SearchRepository searchRepository;

    @Override
    public List<Book> searchByKeyword(String keyword) {
        return searchRepository.searchKeyword(Book.class, keyword, fields);
    }

    @Override
    public List<Book> searchByPhrase(String phrase, int offset, int limit) {
        return searchRepository.searchPhrase(Book.class, phrase, offset, limit, fields);
    }
}
