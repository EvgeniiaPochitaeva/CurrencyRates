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

}
