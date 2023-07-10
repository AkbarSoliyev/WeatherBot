package com.example.services;

import com.example.models.CurrentWeather;

public interface WeatherService {
    CurrentWeather getWeather(String location);

    CurrentWeather getMyWeatherLocation(double lat, double lon);

}
