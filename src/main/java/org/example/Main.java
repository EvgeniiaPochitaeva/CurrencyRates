package org.example;


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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.security.auth.callback.Callback;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.example.client.ApplicationConstants.MONO_BANK;
import static org.example.client.ApplicationConstants.PRIVAT_BANK;


public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException, SchedulerException {
        SchedulerCurrency.Start();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return "CurrencyRates2023Bot";
    }

    @Override
    public String getBotToken() {
        //return "6819128935:AAFD36ZFRbl8b1ZW5RzbmVmle6imI6bgFl0";
        return "6834411073:AAEIUv86SUhblfrDBWoZTKUaKDgcOis_npM";
    }


    @Override
    public void onUpdateReceived(Update update) {

        Long chatId = getChatId(update);
        System.out.println("chatId = " + chatId);
        String emodji = "✅";
        Settings settings = new Settings();
        CurrencyClient currencyClient = new CurrencyClient();
        UserSettings userSettings = settings.getOrCreateUserSettings(chatId);


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
            System.out.println(update.getCallbackQuery().getData());

            if (update.getCallbackQuery().getData().equals("Get info")) {
                settings.getOrCreateUserSettings(chatId);
                String bank = userSettings.getBank();
                String result = null;
                if (bank.equals(PRIVAT_BANK)) {
                   result = currencyClient.getUserPBCurrencyRates(userSettings);
                } else if (bank.equals(MONO_BANK)) {
                    result = currencyClient.getUserMonoCurrencyRates(userSettings);
                } else {
                    currencyClient.getUserNBUCurrencyRates(userSettings);
                }

                SendMessage message = createMessage(result); // данні банка
                message.setChatId(chatId);
                Map<String, String> buttons = new LinkedHashMap<>();
                buttons.put("Отримати інфо", "Get info");
                buttons.put("Налаштування", "Setting");

                attachButtons(message, buttons,2);


                sendApiMethodAsync(message);

            }


            if (update.getCallbackQuery().getData().equals("Setting")) {

                SendMessage message = createMessage("Оберіть налаштування:");
                message.setChatId(chatId);

                Map<String, String> settingButtons = new LinkedHashMap<>();
                settingButtons.put("Валюта", "Currency");
                settingButtons.put("Банк", "Bank");
                settingButtons.put("Кількість знаків \n" + "після коми", "Dot");
                settingButtons.put("Час оповіщення", "Time");

                attachButtons(message, settingButtons,2);

                sendApiMethodAsync(message);
            }


            if (update.getCallbackQuery().getData().equals("Dot")) {
                SendMessage message = createMessage("Оберіть кількість знаків після коми:");
                message.setChatId(chatId);

                String currentDot = userSettings.getDotCount();


                //TODO позначати смайлом яка кількість крапок щас у юзера -  зробила!!
                Map<String, String> dotButtons = new LinkedHashMap<>();

                if (currentDot.equals("2")) {
                    dotButtons.put("2 " + emodji, "dot_2");
                    dotButtons.put("3", "dot_3");
                    dotButtons.put("4", "dot_4");
                } else if (currentDot.equals("3")) {
                    dotButtons.put("2", "dot_2");
                    dotButtons.put("3 " + emodji, "dot_3");
                    dotButtons.put("4", "dot_4");
                } else if (currentDot.equals("4")) {
                    dotButtons.put("2", "dot_2");
                    dotButtons.put("3", "dot_3");
                    dotButtons.put("4 " + emodji, "dot_4");
                }
                attachButtons(message, dotButtons,3);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("dot_2")) {
                SendMessage message = createMessage("Оберіть кількість знаків після коми:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateDot(chatId, 2);
                Map<String, String> dotButtons = new LinkedHashMap<>();

                dotButtons.put("2 " +  emodji , "dot_2");
                dotButtons.put("3 " , "dot_3");
                dotButtons.put("2 "  , "dot_4");

                attachButtons(message, dotButtons,3);
                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("dot_3")) {
                SendMessage message = createMessage("Оберіть кількість знаків після коми:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateDot(chatId, 2);
                Map<String, String> dotButtons = new LinkedHashMap<>();

                dotButtons.put("2 " , "dot_2");
                dotButtons.put("3 " +  emodji , "dot_3");
                dotButtons.put("4 "  , "dot_4");

                attachButtons(message, dotButtons,3);
                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("dot_4")) {
                SendMessage message = createMessage("Оберіть кількість знаків після коми:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateDot(chatId, 4);
                Map<String, String> dotButtons = new LinkedHashMap<>();

                dotButtons.put("2 " , "dot_2");
                dotButtons.put("3 " , "dot_3");
                dotButtons.put("4 "  +  emodji , "dot_4");

                attachButtons(message, dotButtons,3);
                sendApiMethodAsync(message);

            }


            if (update.getCallbackQuery().getData().equals("Bank")) {
                SendMessage message = createMessage("Оберіть банк:");
                message.setChatId(chatId);

                String currentBank = userSettings.getBank();
                //TODO позначати смайлом який банк щас у юзера -  зробила!!

                Map<String, String> bankButtons = new LinkedHashMap<>();

                if (currentBank.equals("nbu_bank")) {
                    bankButtons.put("НБУ " + emodji, "nbu_bank");
                    bankButtons.put("Приватбанк ", "privat_bank");
                    bankButtons.put("Монобанк", "mono_bank");
                } else if (currentBank.equals("privat_bank")) {
                    bankButtons.put("НБУ ", "nbu_bank");
                    bankButtons.put("Приватбанк " + emodji, "privat_bank");
                    bankButtons.put("Монобанк ", "mono_bank");
                } else if (currentBank.equals("mono_bank")) {
                    bankButtons.put("НБУ ", "nbu_bank");
                    bankButtons.put("Приватбанк ", "privat_bank");
                    bankButtons.put("Монобанк " + emodji, "mono_bank");
                }

                attachButtons(message, bankButtons,2);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("nbu_bank")) {
                SendMessage message = createMessage("Оберіть банк:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateBank(chatId, "nbu_bank");
                Map<String, String> bankButtons = new LinkedHashMap<>();

                bankButtons.put("НБУ " +  emodji , "nbu_bank");
                bankButtons.put("Приватбанк " , "privat_bank");
                bankButtons.put("Монобанк "  , "mono_bank");

                attachButtons(message, bankButtons,2);
                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("privat_bank")) {
                SendMessage message = createMessage("Оберіть банк:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateBank(chatId, "privat_bank");
                Map<String, String> bankButtons = new LinkedHashMap<>();

                bankButtons.put("НБУ " , "nbu_bank");
                bankButtons.put("Приватбанк " +  emodji , "privat_bank");
                bankButtons.put("Монобанк "  , "mono_bank");

                attachButtons(message, bankButtons,2);
                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("mono_bank")) {
                SendMessage message = createMessage("Оберіть банк:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);


                settings.updateBank(chatId, "mono_bank");
                Map<String, String> bankButtons = new LinkedHashMap<>();

                bankButtons.put("НБУ " , "nbu_bank");
                bankButtons.put("Приватбанк " , "privat_bank");
                bankButtons.put("Монобанк "  +  emodji , "mono_bank");

                attachButtons(message, bankButtons,2);
                sendApiMethodAsync(message);

            }



            if (update.getCallbackQuery().getData().equals("Currency")) {
                SendMessage message = createMessage("Оберіть валюту:");
                message.setChatId(chatId);

                boolean currentEuroEnabled = userSettings.isEuroEnabled();
                boolean currentUsdEnabled = userSettings.isUsdEnabled();
                //TODO  позначати смайлом які валюти івумкнуті -  зробила!!

                Map<String, String> currencyButtons = new LinkedHashMap<>();
                String euroText = "EURO " + (currentEuroEnabled ? emodji : "");
                String usdText = "USD " + (currentUsdEnabled ? emodji : "");

                currencyButtons.put(euroText, "EURO");
                currencyButtons.put(usdText, "USD");

                attachButtons(message, currencyButtons,2);

                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("EURO")) {
                SendMessage message = createMessage("Оберіть валюту:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                boolean currentEuroEnabled = userSettings.isEuroEnabled();
                boolean currentUsdEnabled = userSettings.isUsdEnabled();
                settings.updateCurrency(chatId, "euro", !currentEuroEnabled);
                Map<String, String> currencyButtons = new LinkedHashMap<>();
                String euroText = "EURO " + (!currentEuroEnabled ? emodji : "");
                String usdText = "USD " + (currentUsdEnabled ? emodji : "");
                currencyButtons.put(euroText, "EURO");
                currencyButtons.put(usdText, "USD");
                attachButtons(message, currencyButtons,2);
                sendApiMethodAsync(message);

            }
            if (update.getCallbackQuery().getData().equals("USD")) {
                SendMessage message = createMessage("Оберіть валюту:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);
                boolean currentEuroEnabled = userSettings.isEuroEnabled();
                boolean currentUsdEnabled = userSettings.isUsdEnabled();
                settings.updateCurrency(chatId, "usd", !currentUsdEnabled);
                Map<String, String> currencyButtons = new LinkedHashMap<>();
                String euroText = "EURO " + (currentEuroEnabled ? emodji : "");
                String usdText = "USD " + (!currentUsdEnabled ? emodji : "");
                currencyButtons.put(euroText, "EURO");
                currencyButtons.put(usdText, "USD");
                attachButtons(message, currencyButtons,2);
                sendApiMethodAsync(message);

            }

            if (update.getCallbackQuery().getData().equals("Time")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);

                // TODO  позначати смайлом який час увімкнутий у юзера -  зробила!!
                String currentTime = userSettings.getTime();


                Map<String, String> timeButtons = new LinkedHashMap<>();
                timeButtons.put("9 " + (currentTime.equals("9") ? emodji : ""), "Time9");
                timeButtons.put("10 " + (currentTime.equals("10") ? emodji : ""), "Time10");
                timeButtons.put("11 " + (currentTime.equals("11") ? emodji : ""), "Time11");
                timeButtons.put("12 " + (currentTime.equals("12") ? emodji : ""), "Time12");
                timeButtons.put("13 " + (currentTime.equals("13") ? emodji : ""), "Time13");
                timeButtons.put("14 " + (currentTime.equals("14") ? emodji : ""), "Time14");
                timeButtons.put("15 " + (currentTime.equals("15") ? emodji : ""), "Time15");
                timeButtons.put("16 " + (currentTime.equals("16") ? emodji : ""), "Time16");
                timeButtons.put("17 " + (currentTime.equals("17") ? emodji : ""), "Time17");
                timeButtons.put("18 " + (currentTime.equals("18") ? emodji : ""), "Time18");

                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }
                attachButtons(message, timeButtons,5);

                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time9")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time9");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 " +  emodji, "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time10")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time10");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 "+  emodji, "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time11")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time11");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 "+  emodji, "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time12")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time12");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 "+  emodji, "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time13")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time13");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 "+  emodji, "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time14")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time14");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 "+  emodji, "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time15")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time15");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 "+  emodji, "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time16")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time16");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 "+  emodji, "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time17")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time17");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 "+  emodji, "Time17");
                timeButtons.put("18 ", "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
            if (update.getCallbackQuery().getData().equals("Time18")) {
                SendMessage message = createMessage("Оберіть час оповіщення:");
                message.setChatId(chatId);
                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
                sendApiMethodAsync(deleteMessage);

                settings.updateTime(chatId, "Time18");
                Map<String, String> timeButtons = new LinkedHashMap<>();

                timeButtons.put("9 ", "Time9");
                timeButtons.put("10 ", "Time10");
                timeButtons.put("11 ", "Time11");
                timeButtons.put("12 ", "Time12");
                timeButtons.put("13 ", "Time13");
                timeButtons.put("14 ", "Time14");
                timeButtons.put("15 ", "Time15");
                timeButtons.put("16 ", "Time16");
                timeButtons.put("17 ", "Time17");
                timeButtons.put("18 "+  emodji, "Time18");
                if (userSettings.isNotificationEnabled()) {
                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
                } else {
                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
                }

                attachButtons(message, timeButtons,5);
                sendApiMethodAsync(message);
            }
//            if (update.getCallbackQuery().getData().equals("DisableNotification")) {
//                SendMessage message = createMessage("Оберіть час оповіщення:");
//                message.setChatId(chatId);
//                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
//                sendApiMethodAsync(deleteMessage);
//
//                settings.updateNotification(chatId, true);
//                Map<String, String> timeButtons = new LinkedHashMap<>();
//
//                timeButtons.put("9 ", "Time9");
//                timeButtons.put("10 ", "Time10");
//                timeButtons.put("11 ", "Time11");
//                timeButtons.put("12 ", "Time12");
//                timeButtons.put("13 ", "Time13");
//                timeButtons.put("14 ", "Time14");
//                timeButtons.put("15 ", "Time15");
//                timeButtons.put("16 ", "Time16");
//                timeButtons.put("17 ", "Time17");
//                timeButtons.put("18 " , "Time18");
//                if (userSettings.isNotificationEnabled()) {
//                    timeButtons.put("Вимкнути повідомлення "+ emodji, "DisableNotification");
//                } else {
//                    timeButtons.put("Увімкнути повідомлення ", "EnableNotification");
//                }
//            }
//            if (update.getCallbackQuery().getData().equals("EnableNotification")) {
//                SendMessage message = createMessage("Оберіть час оповіщення:");
//                message.setChatId(chatId);
//                DeleteMessage deleteMessage = new DeleteMessage(chatId.toString(), update.getCallbackQuery().getMessage().getMessageId());
//                sendApiMethodAsync(deleteMessage);
//
//                settings.updateNotification(chatId, false);
//                Map<String, String> timeButtons = new LinkedHashMap<>();
//
//                timeButtons.put("9 ", "Time9");
//                timeButtons.put("10 ", "Time10");
//                timeButtons.put("11 ", "Time11");
//                timeButtons.put("12 ", "Time12");
//                timeButtons.put("13 ", "Time13");
//                timeButtons.put("14 ", "Time14");
//                timeButtons.put("15 ", "Time15");
//                timeButtons.put("16 ", "Time16");
//                timeButtons.put("17 ", "Time17");
//                timeButtons.put("18 " , "Time18");
//                if (userSettings.isNotificationEnabled()) {
//                    timeButtons.put("Вимкнути повідомлення ", "DisableNotification");
//                } else {
//                    timeButtons.put("Увімкнути повідомлення "+ emodji, "EnableNotification");
//                }
//
//            }
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