package com.example.weatherapi.model;

import android.util.Log;

import com.example.weatherapi.WeatherInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherClient {
    public static final String WEATHER_URL = "https://apihub.kma.go.kr/api/typ02/openApi/VilageFcstInfoService_2.0/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static WeatherInterface weatherInterface = retrofit.create(WeatherInterface.class);


}
