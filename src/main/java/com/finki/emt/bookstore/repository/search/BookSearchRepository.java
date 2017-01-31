package com.finki.emt.bookstore.repository.search;

import com.finki.emt.bookstore.domain.Book;

import java.util.List;

public interface BookSearchRepository {

    List<Book> searchByKeyword(String keyword);

    List<Book> searchByPhrase(String phrase, int offset, int limit);
}
