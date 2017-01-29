package com.finki.emt.bookstore.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class BillingAddress implements Serializable {

    @NotNull
    @Column(name = "billing_address_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "billing_address_street", nullable = false)
    private String street;

    @NotNull
    @Column(name = "billing_address_city", nullable = false)
    private String city;

    @NotNull
    @Column(name = "billing_address_postal_code", nullable = false)
    private String postalCode;

    @NotNull
    @Column(name = "billing_address_state", nullable = false)
    private String state;

    @NotNull
    @Column(name = "billing_address_country_code")
    private String countryCode;

    public BillingAddress() {
    }

    public BillingAddress(String type, String street, String city, String state,
                          String postalCode, String countryCode) {
        this.type = type;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getType() {
        return type;
    }

    public BillingAddress setType(String type) {
        this.type = type;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public BillingAddress setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getCity() {
        return city;
    }

    public BillingAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public BillingAddress setState(String state) {
        this.state = state;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public BillingAddress setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public BillingAddress setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    @Override
    public String toString() {
        return String.format("BillingAddress{" +
                "type='%s', " +
                "street='%s', " +
                "city='%s', " +
                "state='%s', " +
                "postalCode='%s', " +
                "countryCode='%s', " +
                "}", type, street, city, state, postalCode, countryCode);
    }
}
