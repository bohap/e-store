package com.finki.emt.bookstore.service;

import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.User;
import com.finki.emt.bookstore.web.rest.vm.RegisterVM;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findBySlug(String slug);

    Optional<User> findByEmail(String email);

    User save(User user);

    void delete(long id);

    User create(String name, String email, String password);

    User create(RegisterVM registerVM);

    List<User> findByAuthority(String authority);

    List<User> findAllAdmins();

    List<User> findAllRegularUsers();

    List<Book> findAllFavoritesBuSlug(String slug);
}
