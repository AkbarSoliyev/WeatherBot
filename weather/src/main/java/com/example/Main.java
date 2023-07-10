package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.services.WeatherService;
import com.example.services.WeatherServiceImpl;

public class Main {
    public static void main(String[] args) {
        
        
        WeatherService weatherService=new WeatherServiceImpl();
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TmeBotService(weatherService, "6358537338:AAGszGUIhgPdny3bEfuH55Rll9rkiFzY_GI"));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
