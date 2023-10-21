package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return "CurrencyRates2023Bot";
    }

    @Override
    public String getBotToken() {
        return "6834411073:AAEIUv86SUhblfrDBWoZTKUaKDgcOis_npM";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = getChatId(update);
        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            sendImage("eur-usd-dollar-currency", chatId);
            SendMessage message = createMessage(" *Ласкаво просимо!* \n" +
                    "Цей бот допоможе відслідковувати актуальні курси валют.");
            message.setChatId(chatId);

            Map<String, String> buttons = new LinkedHashMap<>();
            buttons.put("Отримати інфо", "Get info");
            buttons.put("Налаштування", "Setting");

            attachButtons(message, buttons,2);

            sendApiMethodAsync(message);
        }


        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Get info")) {
                SendMessage message = createMessage("Дані банка"); // данні банка
                message.setChatId(chatId);
                sendApiMethodAsync(message);
            }

        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Setting")) {

                SendMessage message = createMessage("Оберіть налаштування:");
                message.setChatId(chatId);

                Map<String, String> settingButtons = new LinkedHashMap<>();
                settingButtons.put("Валюта", "Currency");
                settingButtons.put("Банк", "Bank");
                settingButtons.put("Кількість знаків після коми", "Dot");
                settingButtons.put("Час оповіщення", "Time");

                attachButtons(message, settingButtons,2);

                sendApiMethodAsync(message);
            }
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Dot")) {
                SendMessage message = createMessage("Оберіть кількість знаків після коми:");
                message.setChatId(chatId);

                Map<String, String> dotButtons = new LinkedHashMap<>();
                dotButtons.put("2", "dot_2");
                dotButtons.put("3", "dot_3");
                dotButtons.put("4", "dot_4");

                attachButtons(message, dotButtons,3);

                sendApiMethodAsync(message);
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Bank")) {
                SendMessage message = createMessage("Оберіть банк:");
                message.setChatId(chatId);

                Map<String, String> bankButtons = new LinkedHashMap<>();
                bankButtons.put("НБУ", "bank_nbu");
                bankButtons.put("Приватбанк", "bank_privat");
                bankButtons.put("Монобанк", "bank_mono");

                attachButtons(message, bankButtons,3);

                sendApiMethodAsync(message);
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Currency")) {
                SendMessage message = createMessage("Оберіть валюту:");
                message.setChatId(chatId);

                Map<String, String> currencyButtons = new LinkedHashMap<>();
                currencyButtons.put("USD", "USD");
                currencyButtons.put("EURO", "EURO");

                attachButtons(message, currencyButtons,2);

                sendApiMethodAsync(message);
            }
        }
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("Time")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);

                Map<String, String> timeButtons = new LinkedHashMap<>();
                timeButtons.put("9", "Time9");
                timeButtons.put("10", "Time10");
                timeButtons.put("11", "Time11");
                timeButtons.put("12", "Time12");
                timeButtons.put("13", "Time13");
                timeButtons.put("14", "Time14");
                timeButtons.put("15", "Time15");
                timeButtons.put("16", "Time16");
                timeButtons.put("17", "Time17");
                timeButtons.put("18", "Time18");
                timeButtons.put("Вимкнути повідомлення", "DisableNotification");
                attachButtons(message, timeButtons,5);

                sendApiMethodAsync(message);
            }
        }
    }

    public Long getChatId (Update update){
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
    public void sendImage(String name, Long chatid) {
        SendAnimation animation = new  SendAnimation();

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
//            String buttonText = entry.getKey() + " ✅";
            button.setText(new String(entry.getKey().getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(entry.getValue());
            row.add(button);

            // Определите количество кнопок в одной строке.
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