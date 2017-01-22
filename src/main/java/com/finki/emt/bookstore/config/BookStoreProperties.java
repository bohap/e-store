package com.finki.emt.bookstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "book-store")
public class BookStoreProperties implements Serializable {

    private JWT jwt = new JWT();

    public JWT getJwt() {
        return jwt;
    }

    public void setJwt(JWT jwt) {
        this.jwt = jwt;
    }

    public static class JWT implements Serializable {

        private String secret;

        private long tokenValidInMinutes;

        private long tokenRefreshableInMinutes;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getTokenValidInMinutes() {
            return tokenValidInMinutes;
        }

        public void setTokenValidInMinutes(long tokenValidInMinutes) {
            this.tokenValidInMinutes = tokenValidInMinutes;
        }

        public long getTokenRefreshableInMinutes() {
            return tokenRefreshableInMinutes;
        }

        public void setTokenRefreshableInMinutes(long tokenRefreshableInMinutes) {
            this.tokenRefreshableInMinutes = tokenRefreshableInMinutes;
        }
    }
}
