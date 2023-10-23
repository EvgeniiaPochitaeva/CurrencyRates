package org.example.bank.currency;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NBUCurrency {
    private int r030;
    private String txt;
    private Double rate;
    private String cc;
    private String exchangedate;

}
