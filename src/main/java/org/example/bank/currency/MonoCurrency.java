package org.example.bank.currency;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class MonoCurrency {
    @Getter
    private Integer currencyCodeA;
    @Getter
    private Integer currencyCodeB;
    private Integer date;
    @Getter
    private Double rateSell;
    @Getter
    private Double rateBuy;
    private Double rateCross;

    public void setRateSell(Double rateSell) {
        this.rateSell = rateSell;
    }

    public void setRateBuy(Double rateBuy) {
        this.rateBuy = rateBuy;
    }

    @Override
    public String toString() {
        if (currencyCodeA == 840) {
            return "Курс Монобанк: USD/UAH" + "\n" +
                    "Покупка: " + rateBuy + "\n" +
                    "Продаж:  " + rateSell + "\n";
        }
        if (currencyCodeA == 978) {
            return "Курс Монобанк: EUR/UAH" + "\n" +
                    "Покупка: " + rateBuy + "\n" +
                    "Продаж:  " + rateSell + "\n";
        }
        return "-1";
    }
}
