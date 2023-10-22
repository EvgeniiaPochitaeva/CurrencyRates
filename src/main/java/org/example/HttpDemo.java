package org.example;

import org.example.client.CurrencyClient;
import org.example.client.User;
import org.example.client.UserSettings;


import java.net.URI;

public class HttpDemo {
    public static void main(String[] args) {
        CurrencyClient currencyClient = new CurrencyClient();
        User user = User.builder()
                .id(123)
                .userSettings(new UserSettings("Monobank", true, true, false, "9", "2"))
                .build();

        System.out.println(currencyClient.getUserMonoCurrencyRates(user.getUserSettings()));

        System.out.println(currencyClient.getUserPBCurrencyRates(user.getUserSettings()));

        System.out.println(currencyClient.getUserNBUCurrencyRates(user.getUserSettings()));

    }
}