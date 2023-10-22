package org.example.settings;

import javax.json.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {
    public UserSettings getUserSettings(long user) throws IOException {
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

    private UserSettings createUserSettings(long user) throws IOException {

        JsonReader reader = Json.createReader(new FileReader("userdata.json"));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        JsonObject newValue = Json.createObjectBuilder()
                .add("bank", "monobank")
                .add("usd", true)
                .add("euro", false)
                .add("time", "5")
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
        defaultUserSettings.setBank("monobank");
        defaultUserSettings.setTime("9");
        defaultUserSettings.setUsdEnabled(true);
        defaultUserSettings.setEuroEnabled(false);
        defaultUserSettings.setNotificationEnabled(true);
        defaultUserSettings.setDotCount(String.valueOf(2));

        return defaultUserSettings;
    }

    public void updateBank(long user, String bank) throws IOException {
        updateUserData(user, "bank", bank);
    }
    public void updateCurrency(long user, String currency, boolean setEnabled) throws IOException {
        updateUserData(user, currency, setEnabled);
    }
    public void updateTime(long user, String time) throws IOException {
        updateUserData(user, "time", time);
    }
    public void updateDot(long user, int dot) throws IOException {
        updateUserData(user, "dot", String.valueOf(dot));
    }
    public void updateNotification(long user, boolean setEnabled) throws IOException {
        updateUserData(user, "notification", setEnabled);
    }


    private void updateUserData(long user,String key, String value) throws IOException {
        UserSettings userSettings = getUserSettings(user);
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

    private void updateUserData(long user,String key, Boolean value) throws IOException {
        UserSettings userSettings = getUserSettings(user);
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
