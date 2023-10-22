package org.example.settings;

public class UserSettings {

    private String dotCount;
    private String bank;
    private String time;
    private boolean usd;
    private boolean euro;
    private boolean notification;


    public String getDotCount() {
        return dotCount;
    }

    public void setDotCount(String dotCount) {
        this.dotCount = dotCount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isEuroEnabled() {
        return euro;
    }

    public void setEuroEnabled(boolean euro) {
        this.euro = euro;
    }

    public boolean isUsdEnabled() {
        return usd;
    }

    public void setUsdEnabled(boolean usd) {
        this.usd = usd;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "dotCount='" + dotCount + '\'' +
                ", bank='" + bank + '\'' +
                ", time='" + time + '\'' +
                ", usd=" + usd +
                ", euro=" + euro +
                ", notification=" + notification +
                '}';
    }

    public boolean isNotificationEnabled() {
        return notification;
    }

    public void setNotificationEnabled(boolean notification) {
        this.notification = notification;
    }
}
