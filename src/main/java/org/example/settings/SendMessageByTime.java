package org.example.settings;

import lombok.SneakyThrows;
import org.example.Main;
import org.example.client.CurrencyClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Set;

import static org.example.client.ApplicationConstants.MONO_BANK;
import static org.example.client.ApplicationConstants.PRIVAT_BANK;

public class SendMessageByTime implements Job {

    @SneakyThrows
    public void execute(JobExecutionContext context) {
        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        Set<String> users = jsonObject.keySet();
        Settings settings = new Settings();
        for (String user : users) {
            UserSettings userSettings = settings.getOrCreateUserSettings(Long.parseLong(user));
            sendMessage(userSettings, user);
        }
    }

    @SneakyThrows
    private void sendMessage(UserSettings userSettings, String user) {
        LocalTime time = LocalTime.now();
        String currentHour = String.valueOf(time.getHour());
        CurrencyClient currencyClient = new CurrencyClient();
        Main main = new Main();
        String currentBank = userSettings.getBank();
        String text;
        if (userSettings.isNotificationEnabled() & currentHour.contains(userSettings.getTime()) ) {
            if (currentBank.equals(MONO_BANK)) {
                text = currencyClient.getUserMonoCurrencyRates(userSettings);
            } else if (currentBank.equals(PRIVAT_BANK)) {
                text = currencyClient.getUserPBCurrencyRates(userSettings);
            } else {
                text = currencyClient.getUserNBUCurrencyRates(userSettings);
            }                SendMessage message = new SendMessage(user, new String(text.getBytes(),
                    StandardCharsets.UTF_8));
            main.execute(message);
        }
    }
}