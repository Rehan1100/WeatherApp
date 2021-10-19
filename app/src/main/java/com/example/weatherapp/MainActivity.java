package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView CityName,CurrentDate,Temprature,FeelLike,weatherDescription,wind
            ,Humadity,Uvindex,Visibilty,morningtimeTemp,DaytimeTemp
            ,EveningtimeTemp,NighttimeTemp,Sunset,SunRise;

    RecyclerView HourlyWeatherrecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CityName=findViewById(R.id.cityname);
        CurrentDate=findViewById(R.id.currentDate);
        Temprature=findViewById(R.id.Temprature);
        FeelLike=findViewById(R.id.FeelLike);
        weatherDescription=findViewById(R.id.weatherDescription);
        wind=findViewById(R.id.Winds);
        Humadity=findViewById(R.id.Humidity);
        Uvindex=findViewById(R.id.UvIndex);
        Visibilty=findViewById(R.id.Visibilty);
        morningtimeTemp=findViewById(R.id.morningtemprature);
        DaytimeTemp=findViewById(R.id.daytimeTemp);
        EveningtimeTemp=findViewById(R.id.eveningTemprature);
        NighttimeTemp=findViewById(R.id.NightTemprature);
        HourlyWeatherrecyclerView=findViewById(R.id.HourlyWeatherRecylerView);
        Sunset=findViewById(R.id.sunset);
        SunRise=findViewById(R.id.sunrise);





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
        switch (id){
            case R.id.unitf:
                Toast.makeText(getApplicationContext(),"unitf Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.daily:
                Toast.makeText(getApplicationContext(),"daily Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.location:
                Toast.makeText(getApplicationContext(),"location 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}