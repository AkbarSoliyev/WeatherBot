package com.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.example.models.CurrentWeather;
import com.example.services.WeatherService;

public class TmeBotService extends TelegramLongPollingBot {

    WeatherService weatherService;
    boolean addressWrite = false;

    public TmeBotService(WeatherService weatherService, String botToken) {
        super(botToken);
        this.weatherService = weatherService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String username = update.getMessage().getChat().getFirstName();
            System.out.println(update.getMessage().toString());
            printLogs(username, message_text);
            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            if (update.getMessage().hasLocation()) {
                double lon = update.getMessage().getLocation().getLongitude();
                double lat = update.getMessage().getLocation().getLatitude();
                // System.out.println(lon + " --> " + lat);

                CurrentWeather locationWeather = weatherService.getMyWeatherLocation(lat, lon);
                // DeleteMessage deleteMessage = new DeleteMessage();
                // deleteMessage.setMessageId(update.getMessage().getMessageId());
                // deleteMessage.setChatId(update.getMessage().getChatId());
                // try {
                // execute(deleteMessage);
                // } catch (TelegramApiException e) {
                // e.printStackTrace();
                // }

                // SendMessage message = new SendMessage();
                // message.setChatId(update.getMessage().getChatId());

                printWeather(locationWeather, message);
            }

            if (message_text.contains("/start")) {
                // CurrentWeather currentWeather = weatherService.getMyWeather("Moscow");
                // System.out.println(currentWeather);
                // message.setText(currentWeather.toString());
                addressWrite = false;
                message.setText("Assalamu alaykum " + username);

                ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
                markup.setResizeKeyboard(true);
                List<KeyboardRow> keyboardRowList = new ArrayList<>();
                KeyboardRow row = new KeyboardRow();
                KeyboardButton keyboardButton = new KeyboardButton();
                keyboardButton.setRequestLocation(true);
                keyboardButton.setText("My location's weather");
                row.add(keyboardButton);
                keyboardRowList.add(row);
                KeyboardRow row2 = new KeyboardRow();
                KeyboardButton keyboardButton2 = new KeyboardButton();
                keyboardButton2.setText("Search by city");
                row2.add(keyboardButton2);
                keyboardRowList.add(row2);
                markup.setKeyboard(keyboardRowList);
                message.setReplyMarkup(markup);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            if (message_text.contains("Search by city") || addressWrite) {
                if (!addressWrite) {
                    addressWrite = true;
                    System.out.println("kirdim");
                    message.setText("Write your city : ");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    CurrentWeather currentWeather = weatherService.getWeather(message_text);
                    System.out.println(currentWeather);
                    if (currentWeather == null) {
                        message.setText("Bunday shahar mavjud emas :(");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    printWeather(currentWeather, message);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
            // if (weatherService.getMyWeather(message_text) != null) {
            // CurrentWeather currentWeather = weatherService.getMyWeather(message_text);
            // System.out.println(currentWeather);
            // message.setText(currentWeather.toString());
            // try {
            // execute(message);
            // } catch (TelegramApiException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // } else {
            // message.setText("Kiritilgan shahar topilmadi!!!\nIltimos qaytadan urinib
            // ko'ring...");
            // }
        }

    }

    @Override
    public String getBotUsername() {
        return "WeatherBot";
    }

    private void printLogs(String username, String text) {
        System.out.println(username + " -----> " + text);
    }

    private void printWeather(CurrentWeather currentWeather, SendMessage sendMessage) {
        String icon = getIcon(currentWeather.getWeather().get(0).getMain());
        String temp = Integer.toString(Math.round(currentWeather.getMain().getTemp()));
        // String tempToday = (currentWeather.getMain().getTemp_min() > 0 ? "+" : "")
        // + Math.round(currentWeather.getMain().getTemp_min()) + "\u00B0C " + "..."
        // + (currentWeather.getMain().getTemp_max() > 0 ? "+" : "")
        // + Math.round(currentWeather.getMain().getTemp_max()) + "\u00B0C ";
        String sendText = "*******************************************************" +
                "\n\n\uD83C\uDF06 Region : " + currentWeather.getName() +
                "\n\uD83C\uDF21 Temperature : " + (currentWeather.getMain().getTemp() > 0 ? "+" : "") + temp
                + "\u00B0C " +
                "\n\uD83D\uDCA8 Wind : " + currentWeather.getWind().getSpeed() + "m/s " +
                "\nFeeling : " + currentWeather.getWeather().get(0).getMain() +
                "\n\n*******************************************************";
        sendMessage.setText(sendText);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    String getIcon(String main) {
        String icon = "🤷🏻‍♂️";
        switch (main) {
            case "Clouds":
                icon = "☁️";
                break;
            case "Sunny":
                icon = "☀️";
                break;
            case "Rainy":
                icon = "\u1F327";
                break;

        }
        return icon;
    }

}
