package org.example.client;

import io.netty.util.internal.MathUtil;
import org.example.bank.currency.MonoCurrency;
import org.example.bank.currency.NBUCurrency;
import org.example.bank.currency.PBCurrency;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.*;
import org.example.settings.UserSettings;

import static org.example.client.ApplicationConstants.PB_URI;
import static org.example.client.ApplicationConstants.MONO_URI;
import static org.example.client.ApplicationConstants.NBU_URI;
import static org.example.client.ApplicationConstants.EUR_CODE;
import static org.example.client.ApplicationConstants.USD_CODE;
import static org.example.client.ApplicationConstants.UAH_CODE;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class CurrencyClient {
    private HttpClient httpClient = HttpClient.newHttpClient();
    private Gson gson = new Gson();

    @SneakyThrows
    public List<MonoCurrency> getMonoCurrencyRates(URI uri) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //List<PBCurrency> currencyRates = objectMapper.readValue(response.body(), new TypeReference<List<PBCurrency>>(){});
        List<MonoCurrency> currencyRates = gson.fromJson(response.body(), new TypeToken<List<MonoCurrency>>() {
        }.getType());
        //currencyRates.forEach(System.out::println);
        return currencyRates;
    }

    @SneakyThrows
    public List<PBCurrency> getPBCurrencyRates(URI uri) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //List<PBCurrency> currencyRates = objectMapper.readValue(response.body(), new TypeReference<List<PBCurrency>>(){});
        List<PBCurrency> currencyRates = gson.fromJson(response.body(), new TypeToken<List<PBCurrency>>() {
        }.getType());
        //currencyRates.forEach(System.out::println);
        return currencyRates;
    }

    @SneakyThrows
    public List<NBUCurrency> getNBUCurrencyRates(URI uri) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //List<PBCurrency> currencyRates = objectMapper.readValue(response.body(), new TypeReference<List<PBCurrency>>(){});
        List<NBUCurrency> currencyRates = gson.fromJson(response.body(), new TypeToken<List<NBUCurrency>>() {
        }.getType());
        //currencyRates.forEach(System.out::println);
        return currencyRates;
    }



    //        if (user.getUserSettings().getBank().equals("Monobank"))
    @SneakyThrows
    public List<MonoCurrency> getUserMonoCurrencyRates(UserSettings userSettings) {
        //int dot = Integer.parseInt(userSettings.getDot());

        if (userSettings.isEuroEnabled() && userSettings.getUsd().equals(false)) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCurrencyCodeA() == EUR_CODE && s.getCurrencyCodeB() == UAH_CODE)
                    //.map(s -> Math.round(s.getRateSell(), dot))
                    .toList();

        }
        if (!userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCurrencyCodeA() == USD_CODE && s.getCurrencyCodeB() == UAH_CODE)
                    .toList();
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            return currencyRates.stream()
                    .filter(s -> (s.getCurrencyCodeA() == EUR_CODE || s.getCurrencyCodeA() == USD_CODE)
                            && s.getCurrencyCodeB() == UAH_CODE)
                    .toList();
        }
        return null;

    }

    @SneakyThrows
    public List<PBCurrency> getUserPBCurrencyRates(UserSettings userSettings) {
        if (userSettings.isEuroEnabled() && !userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCcy().equals("EUR"))
                    .toList();

        }
        if (!userSettings.isUsdEnabled() && userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCcy().equals("USD"))
                    .toList();
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            return currencyRates;
        }
        return null;
    }

    @SneakyThrows
    public List<NBUCurrency> getUserNBUCurrencyRates(UserSettings userSettings) {
        if (userSettings.isEuroEnabled() && !userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCc().equals("EUR"))
                    .toList();

        }
        if (!userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCc().equals("USD"))
                    .toList();
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            return currencyRates.stream()
                    .filter(s -> s.getCc().equals("EUR") || s.getCc().equals("USD"))
                    .toList();
        }
        return null;

    }
}

/*    @SneakyThrows
    public List<PBCurrency> getMonoCurrencyRate (URI uri) {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //List<PBCurrency> currencyRates = objectMapper.readValue(response.body(), new TypeReference<List<PBCurrency>>(){});
        List<PBCurrency> currencyRates = gson.fromJson(response.body(), new TypeToken<List<PBCurrency>>(){}.getType());
        currencyRates.forEach(System.out::println);
        return currencyRates;
    }

    @SneakyThrows
    public List<PBCurrency> getNBUCurrencyRate (URI uri) {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //List<PBCurrency> currencyRates = objectMapper.readValue(response.body(), new TypeReference<List<PBCurrency>>(){});
        List<PBCurrency> currencyRates = gson.fromJson(response.body(), new TypeToken<List<PBCurrency>>(){}.getType());
        currencyRates.forEach(System.out::println);
        return currencyRates;
    }*/

