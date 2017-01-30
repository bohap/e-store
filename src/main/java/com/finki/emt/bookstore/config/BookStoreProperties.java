package com.finki.emt.bookstore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Configuration
@ConfigurationProperties(prefix = "book-store")
public class BookStoreProperties implements Serializable {

    private Async async;

    private JWT jwt = new JWT();

    private PayPal payPal = new PayPal();

    private Mail mail = new Mail();

    public Async getAsync() {
        return async;
    }

    public void setAsync(Async async) {
        this.async = async;
    }

    public JWT getJwt() {
        return jwt;
    }

    public void setJwt(JWT jwt) {
        this.jwt = jwt;
    }

    public PayPal getPayPal() {
        return payPal;
    }

    public void setPayPal(PayPal payPal) {
        this.payPal = payPal;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }

    public static class Async {

        private int corePoolSize;

        private int maxPoolSize;

        private int queueCapacity;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
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

    public static class PayPal {

        private String clientId;

        private String clientSecret;

        private String mode;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }

    public static class Mail {

        private String from;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }
}
