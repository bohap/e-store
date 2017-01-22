package com.finki.emt.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
public class Authority implements Serializable {

    @NotNull
    @Id
    @Column(length = 50)
    private String name;

    public Authority() {
    }

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !this.getClass().equals(obj.getClass())) &&
                this.name != null && this.name.equals(((Authority) obj).getName());
    }

    @Override
    public int hashCode() {
        return this.name != null ? this.name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Authority{name='%s'}", name);
    }
}
