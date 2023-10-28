package org.example.client;


import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstants {
    public static final String PB_URI = "https://api.privatbank.ua/p24api/pubinfo?exchange&coursid=11";
    public static final String MONO_URI = "https://api.monobank.ua/bank/currency";
    public static final String NBU_URI = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";
    public static final int UAH_CODE = 980;
    public static final int USD_CODE = 840;
    public static final int EUR_CODE = 978;




    // user settings
    public static final String NBU_BANK = "nbu_bank";
    public static final String MONO_BANK = "mono_bank";
    public static final String PRIVAT_BANK = "privat_bank";

}