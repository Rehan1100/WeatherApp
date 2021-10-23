package com.example.weatherapp.ForcastDaily;

import static com.example.weatherapp.Constants.cityname;
import static com.example.weatherapp.Constants.obj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.weatherapp.R;

public class ForcastActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast);
        recyclerView=findViewById(R.id.forcastRecyclerView);
        getSupportActionBar().setTitle(cityname);

        ForcastDailyAdapter forcastDailyAdapter = new ForcastDailyAdapter(obj.daily,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(forcastDailyAdapter);
        forcastDailyAdapter.notifyDataSetChanged();
    }
}