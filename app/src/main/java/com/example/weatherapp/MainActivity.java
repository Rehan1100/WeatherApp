package com.example.weatherapp;

import static com.example.weatherapp.Constants.cityname;
import static com.example.weatherapp.Constants.obj;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Api.Classes.Hourly;
import com.example.weatherapp.Api.Classes.Root;
import com.example.weatherapp.Api.WeatherApi;
import com.example.weatherapp.ForcastDaily.ForcastActivity;
import com.example.weatherapp.MainPageRecyclerView.HourlyWeatherAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
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
    RelativeLayout main,layout;
    String tempunit;
    Root datanow;
    String city = "Chicago, Illinois";
    ProgressBar progressBar;

    RecyclerView HourlyWeatherrecyclerView;
    SharedPreferences sharedPreferences;

    double lat = 41.8675766;
    double lon = -87.616232;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if(obj!=null){
            setData(obj);
        }

        sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE);
        tempunit = sharedPreferences.getString("tempunit", "F");
        lat = Double.parseDouble(sharedPreferences.getString("lat", "41.8675766"));
        lon = Double.parseDouble(sharedPreferences.getString("lon", "41.8675766"));
        city = sharedPreferences.getString("city", "Chicago, Illinois");
        cityname = city;

        if(hasNetworkConnection())
        dataRequest();

        SwipeRefreshLayout swipe = findViewById(R.id.pulltorefresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (hasNetworkConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    dataRequest();
                }
                else
                    Toast.makeText(MainActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);

            }
        });

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
        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.Progressbar);

        layout.setVisibility(View.GONE);
        main.setVisibility(View.GONE);
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

        CityName.setText(city);
        if (tempunit.equals("C")) {
            int temp;
            temp = convertoC(response.current.temp);
            Temprature.setText(String.valueOf(temp) + "°C");
            temp = convertoC(response.current.feels_like);
            FeelLike.setText("Feels Like " + String.valueOf(temp) + "°C");
            temp = convertoC(response.daily.get(0).temp.morn);
            morningtimeTemp.setText(String.valueOf(temp) + "°C");
            temp = convertoC(response.daily.get(0).temp.day);
            DaytimeTemp.setText(String.valueOf(temp) + "°C");
            temp = convertoC(response.daily.get(0).temp.eve);
            EveningtimeTemp.setText(String.valueOf(temp) + "°C");
            temp = convertoC(response.daily.get(0).temp.night);
            NighttimeTemp.setText(String.valueOf(temp) + "°C");

        } else if (tempunit.equals("F")) {
            int temp;
            temp = convertoF(response.current.temp);
            Temprature.setText(String.valueOf(temp) + "°F");
            temp = convertoF(response.current.feels_like);
            FeelLike.setText("Feels Like " + String.valueOf(temp) + "°F");
            temp = convertoF(response.daily.get(0).temp.morn);
            morningtimeTemp.setText(String.valueOf(temp) + "°F");
            temp = convertoF(response.daily.get(0).temp.day);
            DaytimeTemp.setText(String.valueOf(temp) + "°F");
            temp = convertoF(response.daily.get(0).temp.eve);
            EveningtimeTemp.setText(String.valueOf(temp) + "°F");
            temp = convertoF(response.daily.get(0).temp.night);
            NighttimeTemp.setText(String.valueOf(temp) + "°F");

        }


        String iconCode = "_" + response.current.weather.get(0).icon;
        int iconResId = getResources().getIdentifier(iconCode,
                "drawable", getPackageName());
        WeatherIcon.setImageResource(iconResId);
        Humadity.setText("Humidity: " + String.valueOf(response.current.humidity) + "%");
        float vis = response.current.visibility / 1609;
        Visibilty.setText("Visibility: " + String.valueOf(vis) + "mi");
        Uvindex.setText("UV Index: " + response.current.uvi);
        weatherDescription.setText(response.current.weather.get(0).description + "(" + response.current.clouds + "%" + " Clouds" + ")");

        long unix = response.daily.get(0).sunrise;
        SimpleDateFormat sdfx = new java.text.SimpleDateFormat("hh:mm a");
        Date date = new java.util.Date(unix * 1000L);
        SunRise.setText("Sunrise: " + sdfx.format(date));

        unix = response.daily.get(0).sunset;
        date = new java.util.Date(unix * 1000L);
        Sunset.setText("Sunset: " + sdfx.format(date));

        String deg = getDirection(response.current.wind_deg);
        wind.setText("Wind: " + deg + " at " + response.current.wind_speed + "mph");

        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(response.hourly, MainActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        HourlyWeatherrecyclerView.setLayoutManager(linearLayoutManager);
        HourlyWeatherrecyclerView.setAdapter(hourlyWeatherAdapter);
        hourlyWeatherAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);

        sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE);
        tempunit = sharedPreferences.getString("tempunit", "F");

        if (tempunit.equals("C")) {
            menu.getItem(0).setIcon(R.drawable.units_c);
        } else if (tempunit.equals("F")) {
            menu.getItem(0).setIcon(R.drawable.units_f);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unitf:


                if (datanow!=null){


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String current_unit = sharedPreferences.getString("tempunit", "F");
                    if (current_unit.equals("F")) {
                        item.setIcon(R.drawable.units_c);
                        editor.putString("tempunit", "C");
                        editor.apply();
                        tempunit = "C";
                        setData(datanow);
                    } else if (current_unit.equals("C")) {
                        item.setIcon(R.drawable.units_f);
                        editor.putString("tempunit", "F");
                        editor.apply();
                        tempunit = "F";
                        setData(datanow);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wait to load Data", Toast.LENGTH_SHORT).show();

                }
                return true;
            case R.id.daily:
                if (datanow!=null)
                {
                    cityname = city;
                    startActivity(new Intent(MainActivity.this, ForcastActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Wait to load Data", Toast.LENGTH_SHORT).show();

                }

                return true;
            case R.id.location:

                final String[] inputString = {""};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dailog_text);
                builder.setTitle("Enter a location");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputString[0] = input.getText().toString();
                        if (inputString[0].length() > 0) {
                            double[] latlong = getLatLon(inputString[0]);
                            if (latlong[0] != 0 || latlong[1] != 0) {
                                lat = latlong[0];
                                lon = latlong[1];
                                city = getLocationName(inputString[0]);
                                sharedPreferences = getSharedPreferences("user_settings", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("lat", String.valueOf(lat));
                                editor.putString("lon", String.valueOf(lon));
                                editor.putString("city", city);
                                editor.apply();
                                progressBar.setVisibility(View.VISIBLE);
                                dataRequest();
                            }
                        }
                    }
                });
                builder.show();
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
                Call<Root> call = weatherApi.getWeather(lat, lon, "4a7f66f7868207ca0b55c014ec939235");
                call.enqueue(new Callback<Root>() {
                    @Override
                    public void onResponse(Call<Root> call, Response<Root> response) {
                        setData(response.body());
                        datanow = response.body();
                        obj = datanow;

                        layout.setVisibility(View.VISIBLE);
                        main.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<Root> call, Throwable t) {
                        Toast.makeText(MainActivity.this, R.string.invalid_loc, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
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
            // main.setVisibility(View.VISIBLE);
        } else {
            if(datanow==null) {
                main.setVisibility(View.GONE);
                CurrentDate.setText(R.string.no_internet);
            }
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

    public int convertoF(double tempinK) {
        int tempinF = 0;
        tempinF = (int) ((tempinK - 273.15) * 9 / 5 + 32);
        return tempinF;
    }

    public int convertoC(double tempinK) {
        int tempinC = 0;
        tempinC = (int) (tempinK - 273.15);
        return tempinC;
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            lat = address.get(0).getLatitude();
            lon = address.get(0).getLongitude();

            return new double[]{lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            String locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }


}