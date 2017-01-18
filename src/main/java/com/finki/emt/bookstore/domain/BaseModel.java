package com.finki.emt.bookstore.domain;

import javax.persistence.*;

/**
 * Base model from which all models that have a auto incrementing primary key should extend.
 */
@MappedSuperclass
abstract public class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    BaseModel() {
    }

    BaseModel(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
