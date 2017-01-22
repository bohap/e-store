package com.finki.emt.bookstore.repository.util;

import com.finki.emt.bookstore.domain.Authority;
import com.finki.emt.bookstore.domain.Book;
import com.finki.emt.bookstore.domain.Category;
import com.finki.emt.bookstore.domain.User;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.ApplicationContext;

import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RepositoryUtil {

    private ApplicationContext context;

    private Connection connection;

    public RepositoryUtil(ApplicationContext context) {
        this.context = context;
    }

    public void open() {
        DataSource dataSource = context.getBean(DataSource.class);
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection = null;
        }
    }

    private void executeStatements(String... statements) {
        try {
            Statement statement = connection.createStatement();
            for (String s : statements) {
                statement.execute(s);
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Reset the table auto increment id column.
     */
    private void clearTable(String table, boolean reset) {
        String delete = String.format("DELETE from %s", table);
        String resetSql = String.format("ALTER TABLE %s ALTER COLUMN id RESTART WITH 1;", table);
        if (reset) {
            executeStatements(delete, resetSql);
        } else {
            executeStatements(delete);
        }
    }

    /**
     * Reset the table auto increment id column.
     */
    public <T> void clearTable(Class<T> model) {
        String tableName = model.getAnnotation(Table.class).name();
        this.clearTable(tableName, false);
    }

    /**
     * Clear all the data from the table and reset the table auto increment id column to 0.
     */
    public <T> void clearTableAndResetId(Class<T> model) {
        String tableName = model.getAnnotation(Table.class).name();
        this.clearTable(tableName, true);
    }

    /**
     * Reset the table auto increment id column.
     */
    public <T> void clearTable(Class<T> model, String field) {
        String tableName = getRelationTableName(model.getDeclaredFields(), field);
        this.clearTable(tableName, false);
    }

    /**
     * Convert the ZoneDateTime instance to sql DATETIME
     */
    private String mapToDateTime(ZonedDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Get the table name for Many-To-Many relation.
     */
    private String getRelationTableName(Field[] fields, String field) {
        for (Field f : fields) {
            if (f.getName().equals(field)) {
                return f.getAnnotation(JoinTable.class).name();
            }
        }
        return null;
    }

    private String createStoreSqlQuery(String table, String columns, String values) {
        return String.format("insert into %s %s VALUES %s", table, columns, values);
    }

    /**
     * ---------------------------------------
     * Authority model
     * ---------------------------------------
     */
    public void storeAuthorities(Authority... authorities) {
        String table = Authority.class.getAnnotation(Table.class).name();
        String columns = "(name)";
        String values = Arrays.stream(authorities)
                .map(a -> String.format("('%s')", a.getName()))
                .collect(Collectors.joining(", "));
        executeStatements(createStoreSqlQuery(table, columns, values));
    }

    /**
     * ---------------------------------------
     * User model
     * ---------------------------------------
     */

    public void storeUsers(User... users) {
        String table = User.class.getAnnotation(Table.class).name();
        String columns = "(name, slug, email, password, created_at, updated_at)";
        String values = Arrays.stream(users)
                .map(u -> String.format("('%s', '%s', '%s', '%s', '%s', '%s')",
                        u.getName(), u.getSlug(), u.getEmail(), u.getPassword(),
                        mapToDateTime(u.getCreatedAt()), mapToDateTime(u.getUpdatedAt())))
                .collect(Collectors.joining(", "));
        executeStatements(createStoreSqlQuery(table, columns, values));
        this.storeUserAuthorities(users);
    }

    /**
     * ---------------------------------------
     * User Authorities relation
     * ---------------------------------------
     */
    private void storeUserAuthorities(User... users) {
        String table = getRelationTableName(User.class.getDeclaredFields(), "authorities");
        String columns = "(user_id, authority_name)";
        String values = Arrays.stream(users)
                .filter(u -> u.getAuthorities() != null)
                .map(u -> u.getAuthorities().stream()
                            .map(a -> String.format("(%d, '%s')", u.getId(), a.getName()))
                            .collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));
        if (values.length() > 0) {
            executeStatements(createStoreSqlQuery(table, columns, values));
        }
    }

    /**
     * ---------------------------------------
     * Category model
     * ---------------------------------------
     */
    public void storeCategories(Category... categories) {
        String table = Category.class.getAnnotation(Table.class).name();
        String columns = "(name)";
        String values = Arrays.stream(categories)
                .map(c -> String.format("('%s')", c.getName()))
                .collect(Collectors.joining(", "));
        executeStatements(createStoreSqlQuery(table, columns, values));
    }

    /**
     * Book model
     */
    public void storeBooks(Book... books) {
        String table = Book.class.getAnnotation(Table.class).name();
        String columns = "(admin_id, name, slug, short_description, body, created_at, updated_at, " +
                "price, image, publisher, author, year, pages)";
        for (Book book : books) {
            String values = "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement stm =
                        connection.prepareStatement(createStoreSqlQuery(table, columns, values));
                ByteArrayInputStream stream = new ByteArrayInputStream(book.getImage());
                stm.setLong(1, book.getAdmin().getId());
                stm.setString(2, book.getName());
                stm.setString(3, book.getSlug());
                stm.setString(4, book.getShortDescription());
                stm.setString(5, book.getBody());
                stm.setTimestamp(6, Timestamp.from(book.getCreatedAt().toInstant()));
                stm.setTimestamp(7, Timestamp.from(book.getUpdatedAt().toInstant()));
                stm.setDouble(8, book.getPrice());
                stm.setBinaryStream(9, stream, book.getImage().length);
                stm.setString(10, book.getPublisher());
                stm.setString(11, book.getAuthor());
                stm.setInt(12, book.getYear());
                stm.setInt(13, book.getPages());
                stm.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.storeBooksCategories(books);
    }

    /**
     * Books Categories relation
     */
    private void storeBooksCategories(Book... books) {
        String tableName = this.getRelationTableName(Book.class.getDeclaredFields(), "categories");
        String columns = "(book_id, category_id)";
        String values = Arrays.stream(books)
                .filter(b -> b.getCategories() != null)
                .map(b -> b.getCategories().stream()
                        .map(c -> String.format("(%d, %s)", b.getId(), c.getId()))
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));
        if (values.length() > 0) {
            executeStatements(createStoreSqlQuery(tableName,  columns, values));
        }
    }
}
