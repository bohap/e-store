package com.finki.emt.bookstore.config;

public final class Constants {

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_TESTING = "test";

    // Database constants
    public static final String SPRING_HIBERNATE_DDL_AUTO_PROPERTY = "spring.jpa.hibernate.ddl-auto";
    public static final String APP_DB_SEED_PROPERTY = "book-store.db-seed";

    private Constants() {
    }
}
