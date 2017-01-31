package com.finki.emt.bookstore.repository.search;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.PhraseMatchingContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public <T> List<T> searchKeyword(Class<T> entity, String keyword, String... fields) {
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(entity).get();

        Query query = getKeywordQuery(qb, keyword, fields);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, entity);

        return (List<T>) fullTextQuery.getResultList();
    }

    /**
     * Search for the given model by a phrase.
     *
     * @param entityClass   the entity class that will be searched
     * @param phrase        the phrase for which the entity will be searched
     * @param offset        the starting index from which the results should be taken
     * @param limit         the maximum number of results to be retrieved
     * @param fields        the fields from the entity that will be searched
     * @param <T>           the entity class
     * @return              the search result list
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> searchPhrase(Class<T> entityClass, String phrase,
                                    int offset, int limit, String... fields) {
        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager =
                Search.getFullTextEntityManager(entityManager);

        // create the query using Hibernate Search query DSL
        QueryBuilder qb = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder().forEntity(entityClass).get();

        Query query;

        BooleanJunction<BooleanJunction> bool = qb.bool();
        if (phrase.contains(" ")) {
            String[] tokens = phrase.split(" ");

            // For every word in the phrase create a new keyword query that
            // matches for every field
            for (int i = 0; i < tokens.length - 1; i++) {
                bool.should(getKeywordQuery(qb, tokens[i], fields))
                        .boostedTo(.5f);
            }

            // Add wildcard to the last word that allows the sentence to be unfinished
            bool.should(getWildcardQuery(qb, tokens[tokens.length - 1] + "*", fields))
                    .boostedTo(.3f);

            // Search for exact or approximate sentences to the given phrase
            bool.should(getPhraseQuery(qb, phrase, fields))
                    .boostedTo(3f);
            query = bool.createQuery();
        } else {
            // Add a wildcard to the phrase
            bool.should(getWildcardQuery(qb, phrase.toLowerCase() + "*", fields))
                    .boostedTo(2f);

            // Add a fuzzy query that searches for approximate keywords for the fields
            bool.should(getFuzzyQuery(qb, phrase, fields));
            query = bool.createQuery();
        }

        FullTextQuery fullTextQuery =
                fullTextEntityManager.createFullTextQuery(query, entityClass)
                .setFirstResult(offset)
                .setMaxResults(limit);
        return (List<T>) fullTextQuery.getResultList();
    }

    /**
     * Create a new keyword query.
     */
    private Query getKeywordQuery(QueryBuilder qb, String keyword, String... fields) {
        return qb.keyword()
                .onFields(fields)
                .matching(keyword)
                .createQuery();
    }

    /**
     * Crate a new fuzzy query that searcher for words that differ with characters up to 1.
     */
    private Query getFuzzyQuery(QueryBuilder qb, String keyword, String... fields) {
        return qb.keyword().fuzzy()
                .withEditDistanceUpTo(1)
                .onFields(fields)
                .matching(keyword)
                .createQuery();
    }

    /**
     * Create a phrase query that searches for exact or approximate sentences
     */
    private Query getPhraseQuery(QueryBuilder qb, String sentence,
                                 String... fields) {
        PhraseMatchingContext phraseQuery = qb.phrase().onField(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            phraseQuery = phraseQuery.andField(fields[i]);
        }
        return phraseQuery
                .sentence(sentence)
                .createQuery();
    }

    /**
     * Create a wildcard query that lets the keyword to be unfinished.
     */
    private Query getWildcardQuery(QueryBuilder qb, String keyword,
                                   String... fields) {
        TermMatchingContext phraseQuery = qb.keyword().wildcard().onField(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            phraseQuery = phraseQuery.andField(fields[i]);
        }

        return phraseQuery
                .matching(keyword)
                .createQuery();
    }
}
