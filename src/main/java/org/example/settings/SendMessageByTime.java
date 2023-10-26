package org.example.settings;

import org.example.Main;
import org.example.client.CurrencyClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Set;

public class SendMessageByTime implements Job {
    public void execute(JobExecutionContext context) {
        LocalTime time = LocalTime.now();
        String currentHour = String.valueOf(time.getHour());
        CurrencyClient currencyClient = new CurrencyClient();
        Main main = new Main();

        JsonReader reader;
        try {
            reader = Json.createReader(new FileReader("userdata.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonObject = reader.readObject();
        reader.close();

        Set<String> users = jsonObject.keySet();

        Settings settings = new Settings();
        for (String user : users) {

                UserSettings userSettings = settings.getOrCreateUserSettings(Long.parseLong(user));

                String currentBank = userSettings.getBank();
                String currentDot = userSettings.getDotCount();
                boolean currentEuroEnabled = userSettings.isEuroEnabled();
                boolean currentUsdEnabled = userSettings.isUsdEnabled();
                String currentTime = userSettings.getTime();

                if (userSettings.isNotificationEnabled() & currentHour.contains(currentTime) & userSettings.getBank()
                        .equals("mono_bank")) {
                    String userStringCurrencies = currencyClient.getUserMonoCurrencyRates(userSettings);
                    SendMessage message = new SendMessage(user, new String(userStringCurrencies.getBytes(),
                            StandardCharsets.UTF_8));
                    try {
                        main.execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    //TODO тут ми проходим по кожному юзера і дивимося, якщо зараз такеж время яке у юзера
                    // в налаштуваннях то треба запросити курс валют відповідно до налаштувань, після відправити це юзеру
                    //TODO message.setChatId(user)   -   тут айді це user в For
                }
            if (userSettings.isNotificationEnabled() & currentHour.contains(currentTime) & userSettings.getBank()
                    .equals("privat_bank")) {
                String userStringCurrencies = currencyClient.getUserPBCurrencyRates(userSettings);
                SendMessage message = new SendMessage(user, new String(userStringCurrencies.getBytes(),
                        StandardCharsets.UTF_8));
                try {
                    main.execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
            if (userSettings.isNotificationEnabled() & currentHour.contains(currentTime) & userSettings.getBank()
                    .equals("nbu_bank")) {
                String userStringCurrencies = currencyClient.getUserNBUCurrencyRates(userSettings);
                SendMessage message = new SendMessage(user, new String(userStringCurrencies.getBytes(),
                        StandardCharsets.UTF_8));
                try {
                    main.execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}