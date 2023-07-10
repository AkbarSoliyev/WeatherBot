package com.example.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;

import com.example.models.CurrentWeather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WeatherServiceImpl implements WeatherService {
    String apiKey = "b18b0810c0b69f4ce19e8b2f8041446a";

    @Override
    public CurrentWeather getWeather(String location) {
        try {

            URL url = new URL(
                    "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&appid=" + apiKey);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();
            while ((row = bufferedReader.readLine()) != null)
                stringBuilder.append(row);
            Type typeToken = new TypeToken<CurrentWeather>() {
            }.getType();
            Gson gson = new Gson();
            CurrentWeather currentWeather = gson.fromJson(stringBuilder.toString(), typeToken);
            return currentWeather;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        return null;
    }

    @Override
    public CurrentWeather getMyWeatherLocation(double lat, double lon) {
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" +
                    lat + "&lon=" + lon + "&units=metric&appid=" + apiKey);
            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();
            while ((row = bufferedReader.readLine()) != null)
                stringBuilder.append(row);
            Type typeToken = new TypeToken<CurrentWeather>() {
            }.getType();
            Gson gson = new Gson();
            CurrentWeather currentWeather = gson.fromJson(stringBuilder.toString(), typeToken);
            return currentWeather;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
