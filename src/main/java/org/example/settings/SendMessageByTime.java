package org.example.settings;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Set;

public class SendMessageByTime implements Job {
    public void execute(JobExecutionContext context) {
        LocalTime time = LocalTime.now();
        String currentHour = String.valueOf(time.getHour());

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

                if (userSettings.isNotificationEnabled() & currentHour.contains(currentTime)) {
                    System.out.println("Test done");
                    //TODO тут ми проходим по кожному юзера і дивимося, якщо зараз такеж время яке у юзера
                    // в налаштуваннях то треба запросити курс валют відповідно до налаштувань, після відправити це юзеру
                    //TODO message.setChatId(user)   -   тут айді це user в For



                }

        }
    }
}