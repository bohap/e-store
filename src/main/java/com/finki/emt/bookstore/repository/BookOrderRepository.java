package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.BookOrder;
import com.finki.emt.bookstore.domain.BookOrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookOrderRepository extends JpaRepository<BookOrder, BookOrderId>,
                                             JpaSpecificationExecutor<BookOrder> {
}
