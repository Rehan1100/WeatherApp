package com.example.weatherapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Api.Classes.Hourly;
import com.example.weatherapp.Api.Classes.Root;
import com.example.weatherapp.Api.WeatherApi;
import com.example.weatherapp.MainPageRecyclerView.HourlyWeatherAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView CityName, CurrentDate, Temprature, FeelLike, weatherDescription, wind, Humadity, Uvindex, Visibilty, morningtimeTemp, DaytimeTemp, EveningtimeTemp, NighttimeTemp, Sunset, SunRise;
    ImageView WeatherIcon;
    RelativeLayout main;


    RecyclerView HourlyWeatherrecyclerView;

    double lat=41.8675766;
    double lon= -87.616232;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        hasNetworkConnection();
        dataRequest();

    }

    public void init() {
        CityName = findViewById(R.id.cityname);
        CurrentDate = findViewById(R.id.currentDate);
        Temprature = findViewById(R.id.Temprature);
        FeelLike = findViewById(R.id.FeelLike);
        weatherDescription = findViewById(R.id.weatherDescription);
        wind = findViewById(R.id.Winds);
        Humadity = findViewById(R.id.Humidity);
        Uvindex = findViewById(R.id.UvIndex);
        Visibilty = findViewById(R.id.Visibilty);
        morningtimeTemp = findViewById(R.id.morningtemprature);
        DaytimeTemp = findViewById(R.id.daytimeTemp);
        EveningtimeTemp = findViewById(R.id.eveningTemprature);
        NighttimeTemp = findViewById(R.id.NightTemprature);
        HourlyWeatherrecyclerView = findViewById(R.id.HourlyWeatherRecylerView);
        Sunset = findViewById(R.id.sunset);
        SunRise = findViewById(R.id.sunrise);
        WeatherIcon = findViewById(R.id.weathericon);
        main = findViewById(R.id.mainLayout);
    }

    public void setData(Root response) {
        Temprature.setText(String.valueOf(response.current.temp));
        FeelLike.setText("Feels Like "+String.valueOf(response.current.feels_like));
        String iconCode = "_" + response.current.weather.get(0).icon;
        int iconResId = getResources().getIdentifier(iconCode,
                "drawable", getPackageName());
        WeatherIcon.setImageResource(iconResId);
        Humadity.setText("Humidity: "+String.valueOf(response.current.humidity)+"%");
        Visibilty.setText("Visibility: "+String.valueOf(response.current.visibility));
        Uvindex.setText("UV Index: "+response.current.uvi);
        weatherDescription.setText(response.current.weather.get(0).description+"("+response.current.clouds+"%"+" Clouds"+")");
        morningtimeTemp.setText(String.valueOf(response.daily.get(0).temp.morn));
        DaytimeTemp.setText(String.valueOf(response.daily.get(0).temp.day));
        EveningtimeTemp.setText(String.valueOf(response.daily.get(0).temp.eve));
        NighttimeTemp.setText(String.valueOf(response.daily.get(0).temp.night));

        long unix = response.daily.get(0).sunrise;
        SimpleDateFormat sdfx = new java.text.SimpleDateFormat("hh:mm a");
        Date date = new java.util.Date(unix * 1000L);
        SunRise.setText("Sunset: "+sdfx.format(date));

        unix = response.daily.get(0).sunset;
        date = new java.util.Date(unix * 1000L);
        Sunset.setText("Sunrise: "+sdfx.format(date));

        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(response.hourly,MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false);
        HourlyWeatherrecyclerView.setLayoutManager(linearLayoutManager);
        HourlyWeatherrecyclerView.setAdapter(hourlyWeatherAdapter);
        hourlyWeatherAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unitf:
                Toast.makeText(getApplicationContext(), "unitf Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.daily:
                Toast.makeText(getApplicationContext(), "daily Selected", Toast.LENGTH_LONG).show();
                return true;
            case R.id.location:
                Toast.makeText(getApplicationContext(), "location 3 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dataRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasNetworkConnection()) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                WeatherApi weatherApi = retrofit.create(WeatherApi.class);
                Call<Root> call = weatherApi.getWeather(lat,lon,"d28b9cdc8a0367cfbb6d3156cb504323");
                call.enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        setData(response.body());
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            main.setVisibility(View.VISIBLE);
        } else {
            main.setVisibility(View.GONE);
            CurrentDate.setText(R.string.no_internet);
        }


        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataRequest();
    }
}