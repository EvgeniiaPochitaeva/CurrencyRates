package org.example.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.example.bank.currency.MonoCurrency;
import org.example.bank.currency.NBUCurrency;
import org.example.bank.currency.PBCurrency;
import org.example.settings.UserSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.client.ApplicationConstants.*;

public class CurrencyClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @SneakyThrows
    public List<MonoCurrency> getMonoCurrencyRates(URI uri) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<MonoCurrency>>() {
        }.getType());
    }

    @SneakyThrows
    public List<PBCurrency> getPBCurrencyRates(URI uri) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<PBCurrency>>() {
        }.getType());
    }

    @SneakyThrows
    public List<NBUCurrency> getNBUCurrencyRates(URI uri) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), new TypeToken<List<NBUCurrency>>() {
        }.getType());
    }
    private static List<MonoCurrency> roundMonoCurrency(List<MonoCurrency> currencyRates, int dotCount) {
        currencyRates.forEach(s -> s.setRateBuy(Math.round(s.getRateBuy() * Math.pow(10, dotCount)) / Math.pow(10, dotCount)));
        currencyRates.forEach(s -> s.setRateSell(Math.round(s.getRateSell() * Math.pow(10, dotCount)) / Math.pow(10, dotCount)));
        return currencyRates;
    }

    private static List<PBCurrency> roundPBCurrency(List<PBCurrency> currencyRates, int dotCount) {
        currencyRates.forEach(s -> s.setBuy(String.valueOf(Math.round(Double.parseDouble(s.getBuy()) * Math.pow(10, dotCount)) / Math.pow(10, dotCount))));
        currencyRates.forEach(s -> s.setSale(String.valueOf(Math.round(Double.parseDouble(s.getSale()) * Math.pow(10, dotCount)) / Math.pow(10, dotCount))));
        return currencyRates;
    }
    private static List<NBUCurrency> roundNBUCurrency(List<NBUCurrency> currencyRates, int dotCount) {
        currencyRates.forEach(s -> s.setRate(Math.round(s.getRate() * Math.pow(10, dotCount)) / Math.pow(10, dotCount)));
        return currencyRates;
    }

    @SneakyThrows
    public String getUserMonoCurrencyRates(UserSettings userSettings) throws IOException, InterruptedException {
        int dotCount = Integer.parseInt(userSettings.getDotCount());

        if (userSettings.isEuroEnabled() && !userSettings.isEuroEnabled()) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            List<MonoCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> s.getCurrencyCodeA() == EUR_CODE && s.getCurrencyCodeB() == UAH_CODE)
                    .toList();
            return CurrencyClient.roundMonoCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));

        }
        if (!userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            List<MonoCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> s.getCurrencyCodeA() == USD_CODE && s.getCurrencyCodeB() == UAH_CODE)
                    .toList();
            return CurrencyClient.roundMonoCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<MonoCurrency> currencyRates = new CurrencyClient().getMonoCurrencyRates(URI.create(MONO_URI));
            List<MonoCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> (s.getCurrencyCodeA() == EUR_CODE || s.getCurrencyCodeA() == USD_CODE) && s.getCurrencyCodeB() == UAH_CODE)
                    .toList();
            return CurrencyClient.roundMonoCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        return "-1";

    }

    @SneakyThrows
    public String getUserPBCurrencyRates(UserSettings userSettings) throws IOException, InterruptedException {
        int dotCount = Integer.parseInt(userSettings.getDotCount());
        if (userSettings.isEuroEnabled() && !userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            List<PBCurrency> roundedCurrencyRates = CurrencyClient.roundPBCurrency(currencyRates, dotCount);
            return roundedCurrencyRates.stream()
                    .filter(s -> s.getCcy().equals("EUR"))
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        if (!userSettings.isUsdEnabled() && userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            List<PBCurrency> roundedCurrencyRates = CurrencyClient.roundPBCurrency(currencyRates, dotCount);
            return roundedCurrencyRates.stream()
                    .filter(s -> s.getCcy().equals("USD"))
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<PBCurrency> currencyRates = new CurrencyClient().getPBCurrencyRates(URI.create(PB_URI));
            return CurrencyClient.roundPBCurrency(currencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        return "-1";
    }

    @SneakyThrows
    public String getUserNBUCurrencyRates(UserSettings userSettings) throws IOException, InterruptedException {
        int dotCount = Integer.parseInt(userSettings.getDotCount());
        if (userSettings.isEuroEnabled() && !userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            List<NBUCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> s.getCc().equals("EUR"))
                    .toList();
            return CurrencyClient.roundNBUCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        if (!userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            List<NBUCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> s.getCc().equals("USD"))
                    .toList();
            return CurrencyClient.roundNBUCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        if (userSettings.isEuroEnabled() && userSettings.isUsdEnabled()) {
            List<NBUCurrency> currencyRates = new CurrencyClient().getNBUCurrencyRates(URI.create(NBU_URI));
            List<NBUCurrency> userCurrencyRates = currencyRates.stream()
                    .filter(s -> s.getCc().equals("EUR") || s.getCc().equals("USD"))
                    .toList();
            return CurrencyClient.roundNBUCurrency(userCurrencyRates, dotCount).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("\n"));
        }
        return "-1";

    }
}

