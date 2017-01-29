package com.finki.emt.bookstore.web.rest.vm;

import com.finki.emt.bookstore.domain.Promotion;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

public class PromotionVM implements Serializable {

    @Min(1)
    private double newPrice;

    @NotNull
    private ZonedDateTime start;

    @NotNull
    private ZonedDateTime end;

    public PromotionVM() {
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public Promotion createPromotion() {
        return new Promotion(0, newPrice, start, end);
    }
}
