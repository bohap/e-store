package com.finki.emt.bookstore.repository;

import com.finki.emt.bookstore.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthorityRepository extends JpaRepository<Authority, String>,
                                             JpaSpecificationExecutor<Authority> {
}
