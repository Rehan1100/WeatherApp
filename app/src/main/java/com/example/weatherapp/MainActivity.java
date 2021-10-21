package com.example.weatherapp;

import static com.example.weatherapp.Constants.obj;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.weatherapp.ForcastDaily.ForcastActivity;
import com.example.weatherapp.MainPageRecyclerView.HourlyWeatherAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView CityName, CurrentDate, Temprature, FeelLike, weatherDescription, wind, Humadity, Uvindex, Visibilty, morningtimeTemp, DaytimeTemp, EveningtimeTemp, NighttimeTemp, Sunset, SunRise;
    ImageView WeatherIcon;
    RelativeLayout main;
    String tempunit;
    Root datanow;

    RecyclerView HourlyWeatherrecyclerView;
    SharedPreferences sharedPreferences;

    double lat=41.8675766;
    double lon= -87.616232;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        sharedPreferences = getSharedPreferences("user_settings",MODE_PRIVATE);
        tempunit = sharedPreferences.getString("tempunit","F");


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

        LocalDateTime ldt =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ldt = LocalDateTime.ofEpochSecond(response.current.dt + response.timezone_offset, 0, ZoneOffset.UTC);
            DateTimeFormatter dtf =
                    DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
            String formattedTimeString = ldt.format(dtf);
            CurrentDate.setText(formattedTimeString);
        }


        if(tempunit.equals("C")){
            int temp;
            temp = convertoC(response.current.temp);
            Temprature.setText(String.valueOf(temp)+"°C");
            temp = convertoC(response.current.feels_like);
            FeelLike.setText("Feels Like "+String.valueOf(temp)+"°C");
            temp = convertoC(response.daily.get(0).temp.morn);
            morningtimeTemp.setText(String.valueOf(temp)+"°C");
            temp = convertoC(response.daily.get(0).temp.day);
            DaytimeTemp.setText(String.valueOf(temp)+"°C");
            temp = convertoC(response.daily.get(0).temp.eve);
            EveningtimeTemp.setText(String.valueOf(temp)+"°C");
            temp = convertoC(response.daily.get(0).temp.night);
            NighttimeTemp.setText(String.valueOf(temp)+"°C");

        }
        else if(tempunit.equals("F")){
            int temp;
            temp = convertoF(response.current.temp);
            Temprature.setText(String.valueOf(temp)+"°F");
            temp = convertoF(response.current.feels_like);
            FeelLike.setText("Feels Like "+String.valueOf(temp)+"°F");
            temp = convertoF(response.daily.get(0).temp.morn);
            morningtimeTemp.setText(String.valueOf(temp)+"°F");
            temp = convertoF(response.daily.get(0).temp.day);
            DaytimeTemp.setText(String.valueOf(temp)+"°F");
            temp = convertoF(response.daily.get(0).temp.eve);
            EveningtimeTemp.setText(String.valueOf(temp)+"°F");
            temp = convertoF(response.daily.get(0).temp.night);
            NighttimeTemp.setText(String.valueOf(temp)+"°F");

        }


        String iconCode = "_" + response.current.weather.get(0).icon;
        int iconResId = getResources().getIdentifier(iconCode,
                "drawable", getPackageName());
        WeatherIcon.setImageResource(iconResId);
        Humadity.setText("Humidity: "+String.valueOf(response.current.humidity)+"%");
        float vis = response.current.visibility/1609;
        Visibilty.setText("Visibility: "+String.valueOf(vis)+"mi");
        Uvindex.setText("UV Index: "+response.current.uvi);
        weatherDescription.setText(response.current.weather.get(0).description+"("+response.current.clouds+"%"+" Clouds"+")");

        long unix = response.daily.get(0).sunrise;
        SimpleDateFormat sdfx = new java.text.SimpleDateFormat("hh:mm a");
        Date date = new java.util.Date(unix * 1000L);
        SunRise.setText("Sunset: "+sdfx.format(date));

        unix = response.daily.get(0).sunset;
        date = new java.util.Date(unix * 1000L);
        Sunset.setText("Sunrise: "+sdfx.format(date));

        String deg = getDirection(response.current.wind_deg);
        wind.setText("Wind: "+deg+" at "+response.current.wind_speed+"mph");

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

        sharedPreferences = getSharedPreferences("user_settings",MODE_PRIVATE);
        tempunit = sharedPreferences.getString("tempunit","F");

        if(tempunit.equals("C")){
            menu.getItem(0).setIcon(R.drawable.units_c);
        }
        else if(tempunit.equals("F")){
            menu.getItem(0).setIcon(R.drawable.units_f);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unitf:
                Toast.makeText(getApplicationContext(), "unitf Selected", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String current_unit = sharedPreferences.getString("tempunit","F");
                if(current_unit.equals("F")) {
                    item.setIcon(R.drawable.units_c);
                    editor.putString("tempunit", "C");
                    editor.apply();
                    tempunit = "C";
                    setData(datanow);
                }
                else if(current_unit.equals("C")) {
                    item.setIcon(R.drawable.units_f);
                    editor.putString("tempunit", "F");
                    editor.apply();
                    tempunit = "F";
                    setData(datanow);
                }
                return true;
            case R.id.daily:
                Toast.makeText(getApplicationContext(), "daily Selected", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, ForcastActivity.class));
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
                Call<Root> call = weatherApi.getWeather(lat,lon,"4a7f66f7868207ca0b55c014ec939235");
                call.enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        setData(response.body());
                        datanow = response.body();
                        obj = datanow;
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

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }

    public int convertoF(double tempinK){
        int tempinF = 0;
        tempinF = (int)((tempinK - 273.15) * 9/5 + 32);
        return tempinF;
    }
    public int convertoC(double tempinK){
        int tempinC = 0;
        tempinC = (int)(tempinK - 273.15);
        return tempinC;
    }
}