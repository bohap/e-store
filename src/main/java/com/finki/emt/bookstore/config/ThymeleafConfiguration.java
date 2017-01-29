package com.finki.emt.bookstore.config;

import com.paypal.base.codec.CharEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("mails/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML5");
        emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        emailTemplateResolver.setOrder(1);
        return emailTemplateResolver;
    }

    @Bean
    @Description("Add java 8 date support to the Thymeleaf")
    public Java8TimeDialect templateEngine() {
        return new Java8TimeDialect();
    }

    @Bean
    public TemplateEngine templateEngine(Java8TimeDialect dialect) {
        TemplateEngine engine = new TemplateEngine();
        engine.addDialect(dialect);
        return engine;
    }
}
