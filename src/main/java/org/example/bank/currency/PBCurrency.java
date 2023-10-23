package org.example.bank.currency;

import lombok.*;

@Data
@Builder
public class PBCurrency {
    private String ccy;
    private String base_ccy;
    private String buy;
    private String sale;
}