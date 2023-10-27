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
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Set;

import static org.example.client.ApplicationConstants.MONO_BANK;
import static org.example.client.ApplicationConstants.PRIVAT_BANK;

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
            String currentTime = userSettings.getTime();
            String currentBank = userSettings.getBank();
            String text;
            if (userSettings.isNotificationEnabled() & currentHour.contains(currentTime) ) {
                if (currentBank.equals(MONO_BANK)) {
                    text = currencyClient.getUserMonoCurrencyRates(userSettings);
                } else if (currentBank.equals(PRIVAT_BANK)) {
                    text = currencyClient.getUserPBCurrencyRates(userSettings);
                } else {
                    text = currencyClient.getUserNBUCurrencyRates(userSettings);
                }                SendMessage message = new SendMessage(user, new String(text.getBytes(),
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