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
        LocalTime currentTime = LocalTime.now();
        String currentHour = String.valueOf(currentTime.getHour());

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
        for(String user : users) {
            try {
                UserSettings userSettings = settings.getUserSettings(Long.parseLong(user));
                String userTime = userSettings.getTime();
                if (userSettings.isNotificationEnabled() & currentHour.contains(userTime)) {
                    System.out.println("Test done");
                    //TODO тут треба реалізувати відправку повідомлення
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}