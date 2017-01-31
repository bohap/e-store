package com.finki.emt.bookstore.config.search;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class BuildSearchIndex {

    private final Logger log = LoggerFactory.getLogger(BuildSearchIndex.class);

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void build() {
        log.debug("Building search index");
        try {
            FullTextEntityManager fullTextEntityManager =
                    Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            log.debug("Exception while building the index - {}", e);
        }
    }
}
