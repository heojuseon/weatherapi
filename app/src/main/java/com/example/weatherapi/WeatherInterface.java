package com.example.weatherapi;

import com.example.weatherapi.model.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherInterface {
    @GET("getVilageFcst?authKey=dVhKCqf1RziYSgqn9ec4cw")
    Call<WeatherModel> GetWeather(
            @Query("dataType") String data_type,
            @Query("numOfRows") int num_of_rows,
            @Query("pageNo") int page_no,
            @Query("base_date") String base_date,
            @Query("base_time") String base_time,
            @Query("nx") String nx,
            @Query("ny") String ny
    );
}
