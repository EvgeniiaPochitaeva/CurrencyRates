package org.example.bank.currency;

import lombok.*;

@Data
@Builder
public class PBCurrency {
    @Getter
    private String ccy;
    private String base_ccy;
    @Getter
    private String buy;
    @Getter
    private String sale;

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }
    @Override
    public String toString() {
        return "Курс Приватбанк: " + ccy + "/" + base_ccy + "\n" +
                "Покупка: " + buy + "\n" +
                "Продаж:  " + sale + "\n";
    }

}