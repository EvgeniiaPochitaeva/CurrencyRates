package org.example.bank.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonoCurrency {
    private Integer currencyCodeA;
    private Integer currencyCodeB;
    private Integer date;
    private Double rateSell;
    private Double rateBuy;
    private Double rateCross;

    public Integer getCurrencyCodeA() {
        return currencyCodeA;
    }

    public void setCurrencyCodeA(Integer currencyCodeA) {
        this.currencyCodeA = currencyCodeA;
    }

    public Integer getCurrencyCodeB() {
        return currencyCodeB;
    }

    public void setCurrencyCodeB(Integer currencyCodeB) {
        this.currencyCodeB = currencyCodeB;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Double getRateSell() {
        return rateSell;
    }

    public void setRateSell(Double rateSell) {
        this.rateSell = rateSell;
    }

    public Double getRateBuy() {
        return rateBuy;
    }

    public void setRateBuy(Double rateBuy) {
        this.rateBuy = rateBuy;
    }

    public Double getRateCross() {
        return rateCross;
    }

    public void setRateCross(Double rateCross) {
        this.rateCross = rateCross;
    }
}
