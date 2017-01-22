package com.finki.emt.bookstore;

import com.finki.emt.bookstore.config.Constants;
import com.finki.emt.bookstore.config.database.DBSeeder;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;

@SpringBootApplication
public class BookStoreApplication {

	private Logger log = LoggerFactory.getLogger(BookStoreApplication.class);

	@Inject
	private Environment env;

	@Inject
	private DBSeeder seeder;

	@PostConstruct
	public void initApplication() {
		String hibernateDdlAuto = env.getProperty(Constants.SPRING_HIBERNATE_DDL_AUTO_PROPERTY);
		String dbSeed = env.getProperty(Constants.APP_DB_SEED_PROPERTY);
		if (hibernateDdlAuto != null && hibernateDdlAuto.equals("create") &&
				dbSeed != null && dbSeed.equals("true")) {
			seeder.seed();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(BookStoreApplication.class, args);
	}
}
