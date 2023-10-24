package org.example.settings;

import lombok.SneakyThrows;

import javax.json.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    @SneakyThrows
    public UserSettings getOrCreateUserSettings(long user) {
        File file = new File("userdata.json");
        if (!file.exists()) {

            try (FileWriter fileWriter = new FileWriter("userdata.json")) {
                fileWriter.write("{}");
            } catch (IOException e) {
                throw new IOException();
            }
        }




        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
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
    private UserSettings createUserSettings(long user) {

        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
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

        JsonWriter writer = Json.createWriter(new FileWriter("userdata.json"));
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
    public void updateCurrency(long user, String currency, boolean setEnabled) {
        updateUserData(user, currency, setEnabled);
    }

    @SneakyThrows
    public void updateTime(long user, String time) {
        updateUserData(user, "time", time);
    }

    @SneakyThrows
    public void updateDot(long user, int dot) {
        updateUserData(user, "dot", String.valueOf(dot));
    }

    @SneakyThrows
    public void updateNotification(long user, boolean setEnabled) {
        updateUserData(user, "notification", setEnabled);
    }

    @SneakyThrows
    private void updateUserData(long user,String key, String value){
        UserSettings userSettings = getOrCreateUserSettings(user);
        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        JsonObject newValue = Json.createObjectBuilder()
                .add("bank", userSettings.getBank())
                .add("usd", userSettings.isUsdEnabled())
                .add("notification", userSettings.isNotificationEnabled())
                .add("euro", userSettings.isEuroEnabled())
                .add("time", userSettings.getTime())
                .add("dot", userSettings.getDotCount())
                .add(key, value)
                .build();

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject)
                .add(String.valueOf(user), newValue);

        jsonObject = jsonObjectBuilder.build();

        JsonWriter writer = Json.createWriter(new FileWriter("userdata.json"));
        writer.writeObject(jsonObject);
        writer.close();

    }

    @SneakyThrows

    private void updateUserData(long user,String key, Boolean value) {
        UserSettings userSettings = getOrCreateUserSettings(user);
        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
        JsonObject jsonObject = reader.readObject();
        reader.close();
        JsonObject newValue = Json.createObjectBuilder()
                .add("bank", userSettings.getBank())
                .add("usd", userSettings.isUsdEnabled())
                .add("notification", userSettings.isNotificationEnabled())
                .add("euro", userSettings.isEuroEnabled())
                .add("time", userSettings.getTime())
                .add("dot", userSettings.getDotCount())
                .add(key, value)
                .build();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(jsonObject)
                .add(String.valueOf(user), newValue);
        jsonObject = jsonObjectBuilder.build();
        JsonWriter writer = Json.createWriter(new FileWriter("userdata.json"));
        writer.writeObject(jsonObject);
        writer.close();
    }

}
