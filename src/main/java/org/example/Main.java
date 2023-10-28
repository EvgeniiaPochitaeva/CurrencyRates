package org.example;


import lombok.SneakyThrows;
import org.example.client.CurrencyClient;
import org.example.settings.SchedulerCurrency;
import org.example.settings.Settings;
import org.example.settings.UserSettings;
import org.quartz.SchedulerException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.example.client.ApplicationConstants.*;


public class Main extends TelegramLongPollingBot {
    String emodji = "✅";

    public static void main(String[] args) throws TelegramApiException, SchedulerException {

        SchedulerCurrency.Start();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return BotConstance.BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BotConstance.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        Settings settings = new Settings();
        CurrencyClient currencyClient = new CurrencyClient();
        UserSettings userSettings = settings.getOrCreateUserSettings(chatId);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            handleStartCommand(chatId);
            return;
        }

        if (update.hasCallbackQuery()) {
            String[] keys = update.getCallbackQuery().getData().split(" ");
            switch (keys[0]) {
                case "getInfo" -> handleGetInfoCallback(chatId, settings, currencyClient, userSettings);
                case "getSetting" -> handleSettingCallback(chatId);
                case "getDot" -> handleDotCallback(chatId, userSettings);
                case "getTime" -> handleTimeCallback(chatId, userSettings);
                case "getCurrency" -> handleCurrencyCallback(chatId, userSettings);
                case "getBank" -> handleBankCallback(chatId, userSettings);

                case "setDot" -> handleSetDotCallback(chatId, settings, update);
                case "setBank" -> handleSetBankCallback(chatId, settings, update);
                case "setEURO" -> handleEuroCallback(chatId, userSettings, settings, update);
                case "setUSD" -> handleUsdCallback(chatId, userSettings, settings, update);
                case "setTime" -> handleSetTimeCallback(chatId, settings, userSettings, update);
                case "updateNotification" -> handleUpdateNotificationCallback(chatId, userSettings, settings, update);
            }
        }
    }

    private void handleStartCommand(Long chatId) {
        sendImage("eur-usd-dollar-currency", chatId);
        SendMessage message = createMessage(" *Ласкаво просимо!* \n" +
                "Цей бот допоможе відслідковувати актуальні курси валют.");
        message.setChatId(chatId);
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("Отримати інфо", "getInfo");
        buttons.put("Налаштування", "getSetting");
        attachButtons(message, buttons, 2);
        sendApiMethodAsync(message);
    }

    private void handleGetInfoCallback(Long chatId, Settings settings, CurrencyClient currencyClient, UserSettings userSettings) {
        settings.getOrCreateUserSettings(chatId);
        String bank = userSettings.getBank();

        String result;
        if (bank.equals(PRIVAT_BANK)) {
            result = currencyClient.getUserPBCurrencyRates(userSettings);
        } else if (bank.equals(MONO_BANK)) {
            result = currencyClient.getUserMonoCurrencyRates(userSettings);
        } else {
            result = currencyClient.getUserNBUCurrencyRates(userSettings);
        }
        SendMessage message = createMessage(result);
        message.setChatId(chatId);
        Map<String, String> buttons = new LinkedHashMap<>();
        buttons.put("Отримати інфо", "getInfo");
        buttons.put("Налаштування", "getSetting");

        attachButtons(message, buttons, 2);
        sendApiMethodAsync(message);
    }

    private void handleSettingCallback(Long chatId) {
        SendMessage message = createMessage("Оберіть налаштування:");
        message.setChatId(chatId);

        Map<String, String> settingButtons = new LinkedHashMap<>();
        settingButtons.put("Валюта", "getCurrency");
        settingButtons.put("Банк", "getBank");
        settingButtons.put("Кількість знаків \n" + "після коми", "getDot");
        settingButtons.put("Час оповіщення", "getTime");

        attachButtons(message, settingButtons, 2);
        sendApiMethodAsync(message);
    }

    private void handleDotCallback(Long chatId, UserSettings userSettings) {
        SendMessage message = createMessage("Оберіть кількість знаків після коми:");
        message.setChatId(chatId);
        String currentDot = userSettings.getDotCount();

        Map<String, String> dotButtons = new LinkedHashMap<>();
        dotButtons.put("2 " + (currentDot.equals("2") ? emodji : ""), "setDot 2");
        dotButtons.put("3 " + (currentDot.equals("3") ? emodji : ""), "setDot 3");
        dotButtons.put("4 " + (currentDot.equals("4") ? emodji : ""), "setDot 4");

        attachButtons(message, dotButtons, 3);
        sendApiMethodAsync(message);
    }

    private void handleSetDotCallback(Long chatId, Settings settings, Update update) {
        SendMessage message = createMessage("Оберіть кількість знаків після коми:");
        message.setChatId(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);
        String[] newDot = update.getCallbackQuery().getData().split(" ");
        String currentDot = newDot[1];
        settings.updateDot(chatId, currentDot);
        Map<String, String> dotButtons = new LinkedHashMap<>();
        dotButtons.put("2 " + (currentDot.equals("2") ? emodji : ""), "setDot 2");
        dotButtons.put("3 " + (currentDot.equals("3") ? emodji : ""), "setDot 3");
        dotButtons.put("4 " + (currentDot.equals("4") ? emodji : ""), "setDot 4");

        attachButtons(message, dotButtons, 3);
        sendApiMethodAsync(message);
    }

    private void handleBankCallback(Long chatId, UserSettings userSettings) {
        SendMessage message = createMessage("Оберіть банк:");
        message.setChatId(chatId);
        String currentBank = userSettings.getBank();
        Map<String, String> bankButtons = new LinkedHashMap<>();

        bankButtons.put("НБУ " + (currentBank.equals(NBU_BANK) ? emodji : ""), "setBank " + NBU_BANK);
        bankButtons.put("Приватбанк " + (currentBank.equals(PRIVAT_BANK) ? emodji : ""), "setBank " + PRIVAT_BANK);
        bankButtons.put("Монобанк " + (currentBank.equals(MONO_BANK) ? emodji : ""), "setBank " + MONO_BANK);

        attachButtons(message, bankButtons, 2);
        sendApiMethodAsync(message);
    }

    private void handleSetBankCallback(Long chatId, Settings settings, Update update) {
        SendMessage message = createMessage("Оберіть банк:");
        message.setChatId(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);
        String[] newBank = update.getCallbackQuery().getData().split(" ");
        settings.updateBank(chatId, newBank[1]);
        Map<String, String> bankButtons = new LinkedHashMap<>();
        bankButtons.put("НБУ " + (newBank[1].equals(NBU_BANK) ? emodji : ""), "setBank " + NBU_BANK);
        bankButtons.put("Приватбанк " + (newBank[1].equals(PRIVAT_BANK) ? emodji : ""), "setBank " + PRIVAT_BANK);
        bankButtons.put("Монобанк " + (newBank[1].equals(MONO_BANK) ? emodji : ""), "setBank " + MONO_BANK);
        attachButtons(message, bankButtons, 2);
        sendApiMethodAsync(message);
    }

    private void handleCurrencyCallback(Long chatId, UserSettings userSettings) {
        SendMessage message = createMessage("Оберіть валюту:");
        message.setChatId(chatId);

        boolean currentEuroEnabled = userSettings.isEuroEnabled();
        boolean currentUsdEnabled = userSettings.isUsdEnabled();

        Map<String, String> currencyButtons = new LinkedHashMap<>();
        String euroText = "EURO " + (currentEuroEnabled ? emodji : "");
        String usdText = "USD " + (currentUsdEnabled ? emodji : "");

        currencyButtons.put(euroText, "setEURO");
        currencyButtons.put(usdText, "setUSD");

        attachButtons(message, currencyButtons, 2);

        sendApiMethodAsync(message);
    }

    private void handleEuroCallback(Long chatId, UserSettings userSettings, Settings settings, Update update) {
        SendMessage message = createMessage("Оберіть валюту:");
        message.setChatId(chatId);

        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);

        boolean currentEuroEnabled = userSettings.isEuroEnabled();

        boolean currentUsdEnabled = userSettings.isUsdEnabled();
        settings.updateCurrency(chatId, "euro");
        Map<String, String> currencyButtons = new LinkedHashMap<>();
        String euroText = "EURO " + (!currentEuroEnabled ? emodji : "");
        String usdText = "USD " + (currentUsdEnabled ? emodji : "");
        currencyButtons.put(euroText, "setEURO");
        currencyButtons.put(usdText, "setUSD");
        attachButtons(message, currencyButtons, 2);
        sendApiMethodAsync(message);
    }

    private void handleUsdCallback(Long chatId, UserSettings userSettings, Settings settings, Update update) {
        SendMessage message = createMessage("Оберіть валюту:");
        message.setChatId(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);
        boolean currentEuroEnabled = userSettings.isEuroEnabled();
        boolean currentUsdEnabled = userSettings.isUsdEnabled();
        settings.updateCurrency(chatId, "usd");
        Map<String, String> currencyButtons = new LinkedHashMap<>();
        String euroText = "EURO " + (currentEuroEnabled ? emodji : "");
        String usdText = "USD " + (!currentUsdEnabled ? emodji : "");
        currencyButtons.put(euroText, "setEURO");
        currencyButtons.put(usdText, "setUSD");
        attachButtons(message, currencyButtons, 2);
        sendApiMethodAsync(message);
    }

    private void handleTimeCallback(Long chatId, UserSettings userSettings) {
        SendMessage message = createMessage("Оберіть час оповіщення:");
        message.setChatId(chatId);
        String currentTime = userSettings.getTime();

        Map<String, String> timeButtons = new LinkedHashMap<>();
        timeButtons.put("9 " + (currentTime.equals("9") ? emodji : ""), "setTime 9");
        timeButtons.put("10 " + (currentTime.equals("10") ? emodji : ""), "setTime 10");
        timeButtons.put("11 " + (currentTime.equals("11") ? emodji : ""), "setTime 11");
        timeButtons.put("12 " + (currentTime.equals("12") ? emodji : ""), "setTime 12");
        timeButtons.put("13 " + (currentTime.equals("13") ? emodji : ""), "setTime 13");
        timeButtons.put("14 " + (currentTime.equals("14") ? emodji : ""), "setTime 14");
        timeButtons.put("15 " + (currentTime.equals("15") ? emodji : ""), "setTime 15");
        timeButtons.put("16 " + (currentTime.equals("16") ? emodji : ""), "setTime 16");
        timeButtons.put("17 " + (currentTime.equals("17") ? emodji : ""), "setTime 17");
        timeButtons.put("18 " + (currentTime.equals("18") ? emodji : ""), "setTime 18");

        if (userSettings.isNotificationEnabled()) {
            timeButtons.put("Вимкнути повідомлення ", "updateNotification");
        } else {
            timeButtons.put("Увімкнути повідомлення ", "updateNotification");
        }
        attachButtons(message, timeButtons, 5);

        sendApiMethodAsync(message);

    }

    private void handleSetTimeCallback(Long chatId, Settings settings, UserSettings userSettings, Update update) {
        SendMessage message = createMessage("Оберіть час оповіщення:");
        message.setChatId(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);
        String[] words = update.getCallbackQuery().getData().split(" ");
        String currentTime = words[1];
        settings.updateTime(chatId, currentTime);
        Map<String, String> timeButtons = new LinkedHashMap<>();

        timeButtons.put("9 " + (currentTime.equals("9") ? emodji : ""), "setTime 9");
        timeButtons.put("10 " + (currentTime.equals("10") ? emodji : ""), "setTime 10");
        timeButtons.put("11 " + (currentTime.equals("11") ? emodji : ""), "setTime 11");
        timeButtons.put("12 " + (currentTime.equals("12") ? emodji : ""), "setTime 12");
        timeButtons.put("13 " + (currentTime.equals("13") ? emodji : ""), "setTime 13");
        timeButtons.put("14 " + (currentTime.equals("14") ? emodji : ""), "setTime 14");
        timeButtons.put("15 " + (currentTime.equals("15") ? emodji : ""), "setTime 15");
        timeButtons.put("16 " + (currentTime.equals("16") ? emodji : ""), "setTime 16");
        timeButtons.put("17 " + (currentTime.equals("17") ? emodji : ""), "setTime 17");
        timeButtons.put("18 " + (currentTime.equals("18") ? emodji : ""), "setTime 18");
        if (userSettings.isNotificationEnabled()) {
            timeButtons.put("Вимкнути повідомлення ", "updateNotification");
        } else {
            timeButtons.put("Увімкнути повідомлення ", "updateNotification");
        }

        attachButtons(message, timeButtons, 5);
        sendApiMethodAsync(message);
    }

    private void handleUpdateNotificationCallback(Long chatId, UserSettings userSettings, Settings settings, Update update) {
        SendMessage message = createMessage("Оберіть час оповіщення:");
        message.setChatId(chatId);
        DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
        sendApiMethodAsync(deleteMessage);

        boolean currentNotification = userSettings.isNotificationEnabled();

        Map<String, String> timeButtons = new LinkedHashMap<>();
        String currentTime = userSettings.getTime();

        timeButtons.put("9 " + (currentTime.equals("9") ? emodji : ""), "setTime 9");
        timeButtons.put("10 " + (currentTime.equals("10") ? emodji : ""), "setTime 10");
        timeButtons.put("11 " + (currentTime.equals("11") ? emodji : ""), "setTime 11");
        timeButtons.put("12 " + (currentTime.equals("12") ? emodji : ""), "setTime 12");
        timeButtons.put("13 " + (currentTime.equals("13") ? emodji : ""), "setTime 13");
        timeButtons.put("14 " + (currentTime.equals("14") ? emodji : ""), "setTime 14");
        timeButtons.put("15 " + (currentTime.equals("15") ? emodji : ""), "setTime 15");
        timeButtons.put("16 " + (currentTime.equals("16") ? emodji : ""), "setTime 16");
        timeButtons.put("17 " + (currentTime.equals("17") ? emodji : ""), "setTime 17");
        timeButtons.put("18 " + (currentTime.equals("18") ? emodji : ""), "setTime 18");
        if (!currentNotification) {
            timeButtons.put("Вимкнути повідомлення", "updateNotification");
        } else {
            timeButtons.put("Увімкнути повідомлення", "updateNotification");
        }
        attachButtons(message, timeButtons, 5);
        sendApiMethodAsync(message);
        settings.updateNotification(chatId);
    }

    public Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return null;
    }

    public SendMessage createMessage(String text) {
        SendMessage message = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        return message;
    }

    @SneakyThrows
    public void sendImage(String name, Long chatid) {
        SendAnimation animation = new SendAnimation();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File("images/" + name + ".gif"));

        animation.setAnimation(inputFile);
        animation.setChatId(chatid);

        executeAsync(animation);
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons, int buttonsPerRow) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(entry.getKey().getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(entry.getValue());
            row.add(button);
            if (row.size() == buttonsPerRow) {
                keyboard.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) {
            keyboard.add(row);
        }
        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }
}