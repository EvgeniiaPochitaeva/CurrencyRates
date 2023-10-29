package org.example.bank.currency;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class NBUCurrency {
    private int r030;
    private String txt;
    @Getter
    private Double rate;
    @Getter
    private String cc;
    private String exchangedate;

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Курс НБУ " + cc + "/UAH: " + rate + "\n";
    }

}
