package com.example.weatherapp.Api;

import com.example.weatherapp.Api.Classes.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/onecall")
    Call<Root> getWeather(@Query("lat") double lat,
                          @Query("lon") double lon,
                          @Query("appid") String apikey);
}
