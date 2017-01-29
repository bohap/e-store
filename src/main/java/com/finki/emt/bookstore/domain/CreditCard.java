package com.finki.emt.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class CreditCard {

    @NotNull
    @Column(name = "credit_card_type", nullable = false)
    private String type;

    @NotNull
    @Size(min = 16, max = 16)
    @Column(name = "credit_card__number", nullable = false, length = 16)
    private String number;

    @Size(min = 1, max = 12)
    @Column(name = "credit_card__expire_month", nullable = false)
    private int expireMonth;

    @Size(min = 2017)
    @Column(name = "credit_card__expire_year", nullable = false)
    private int expireYear;

    @Size(min = 3, max = 3)
    @Column(name = "credit_card__cvv2", nullable = false, length = 3)
    private String cvv2;

    public CreditCard() {
    }

    public CreditCard(String type, String number, int expireMonth, int expireYear, String cvv2) {
        this.type = type;
        this.number = number;
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
        this.cvv2 = cvv2;
    }

    public String getType() {
        return type;
    }

    public CreditCard setType(String type) {
        this.type = type;
        return this;
    }

    public String getNumber() {
        return number;
    }

    public CreditCard setNumber(String number) {
        this.number = number;
        return this;
    }

    public int getExpireMonth() {
        return expireMonth;
    }

    public CreditCard setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
        return this;
    }

    public int getExpireYear() {
        return expireYear;
    }

    public CreditCard setExpireYear(int expireYear) {
        this.expireYear = expireYear;
        return this;
    }

    public String getCvv2() {
        return cvv2;
    }

    public CreditCard setCvv2(String cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Credit{" +
                "type='%s', " +
                "number'%s', " +
                "expireMonth=%d, " +
                "expireYear=%d, " +
                "cvv2='%s', " +
                "}", type, number, expireMonth, expireYear, cvv2);
    }
}
