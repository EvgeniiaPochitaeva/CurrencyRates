package org.example.client;

/*Класс-заглушка для UserSettings*/

import lombok.Builder;
import lombok.Data;

@Data
public class UserSettings {
    private String bank;
    private Boolean usd;
    private Boolean notification;
    private Boolean euro;
    private String time;
    private String dot;

    public UserSettings() {
    }

    public UserSettings(String bank, Boolean usd, Boolean notification, Boolean euro, String time, String dot) {
        this.bank = bank;
        this.usd = usd;
        this.notification = notification;
        this.euro = euro;
        this.time = time;
        this.dot = dot;
    }
}