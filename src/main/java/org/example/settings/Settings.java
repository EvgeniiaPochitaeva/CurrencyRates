package org.example.settings;

import lombok.SneakyThrows;

import javax.json.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    public static final String userdataFileName = "userdata.json";

    @SneakyThrows
    public UserSettings getOrCreateUserSettings(long user) {
        checkExistFile();


        JsonReader reader = Json.createReader(new FileReader(userdataFileName));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        if (jsonObject.getJsonObject(String.valueOf(user)) != null) {
            JsonObject userData = jsonObject.getJsonObject(String.valueOf(user));
            UserSettings userSettings = new UserSettings();
            userSettings.setBank(userData.getString("bank"));
            userSettings.setEuroEnabled(userData.getBoolean("euro"));
            userSettings.setNotificationEnabled(userData.getBoolean("notification"));
            userSettings.setUsdEnabled(userData.getBoolean("usd"));
            userSettings.setTime(userData.getString("time"));
            userSettings.setDotCount(userData.getString("dot"));

            return userSettings;
        } else {
            return createUserSettings(user);
        }

    }

    @SneakyThrows
    private void checkExistFile() {
        File file = new File(userdataFileName);
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(userdataFileName)) {
                fileWriter.write("{}");
            } catch (IOException e) {
                throw new IOException();
            }
        }
    }

    @SneakyThrows
    private UserSettings createUserSettings(long user) {
        JsonReader reader = Json.createReader(new FileReader(userdataFileName));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        JsonObject newValue = Json.createObjectBuilder()
                .add("bank", "privat_bank")
                .add("usd", true)
                .add("euro", false)
                .add("time", "9")
                .add("dot", "2")
                .add("notification", true)
                .build();

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject)
                .add(String.valueOf(user), newValue);

        jsonObject = jsonObjectBuilder.build();

        JsonWriter writer = Json.createWriter(new FileWriter(userdataFileName));
        writer.writeObject(jsonObject);
        writer.close();

        UserSettings defaultUserSettings = new UserSettings();
        defaultUserSettings.setBank("privat_bank");
        defaultUserSettings.setTime("9");
        defaultUserSettings.setUsdEnabled(true);
        defaultUserSettings.setEuroEnabled(false);
        defaultUserSettings.setNotificationEnabled(true);
        defaultUserSettings.setDotCount(String.valueOf(2));

        return defaultUserSettings;
    }


    @SneakyThrows
    public void updateBank(long user, String bank) {
        updateUserData(user, "bank", bank);
    }

    @SneakyThrows
    public void updateCurrency(long user, String currency) {
        updateUserData(user, currency, null);
    }

    @SneakyThrows
    public void updateTime(long user, String time) {
        updateUserData(user, "time", time);
    }

    @SneakyThrows
    public void updateDot(long user, String dot) {
        updateUserData(user, "dot", dot);
    }

    @SneakyThrows
    public void updateNotification(long user) {
        updateUserData(user, "notification", null);
    }


    @SneakyThrows
    private void updateUserData(long user, String key, String value) {
        UserSettings userSettings = getOrCreateUserSettings(user);
        JsonReader reader = Json.createReader(new FileReader(userdataFileName));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        JsonObjectBuilder newValue = Json.createObjectBuilder();
        newValue.add("bank", userSettings.getBank());
        newValue.add("time", userSettings.getTime());
        newValue.add("dot", userSettings.getDotCount());
        newValue.add("euro", userSettings.isEuroEnabled());
        newValue.add("usd", userSettings.isUsdEnabled());
        newValue.add("notification", userSettings.isNotificationEnabled());
        switch (key) {
            case "euro" -> newValue.add("euro", !userSettings.isEuroEnabled());
            case "usd" -> newValue.add("usd", !userSettings.isUsdEnabled());
            case "notification" -> newValue.add("notification", !userSettings.isNotificationEnabled());
            default -> newValue.add(key, value);
        }
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject)
                .add(String.valueOf(user), newValue.build());
        jsonObject = jsonObjectBuilder.build();
        JsonWriter writer = Json.createWriter(new FileWriter(userdataFileName));
        writer.writeObject(jsonObject);
        writer.close();
    }

}
