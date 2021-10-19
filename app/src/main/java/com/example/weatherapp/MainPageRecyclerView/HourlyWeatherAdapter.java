package com.example.weatherapp.MainPageRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.weatherapp.Api.Classes.Hourly;
import com.example.weatherapp.R;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.Viewholder> implements Filterable {

    Context context;
    List<Hourly> data;

    public HourlyWeatherAdapter(List<Hourly> data, Context context) {
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_page_row_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherAdapter.Viewholder holder, int position) {
        Hourly obj = data.get(position);
        holder.desc.setText(obj.weather.get(0).description);
        holder.temp.setText(String.valueOf(obj.temp));

        String iconCode = "_" + obj.weather.get(0).icon;
        int iconResId = context.getResources().getIdentifier(iconCode,
                "drawable", context.getPackageName());
        holder.icon.setImageResource(iconResId);

        long unix = obj.dt;
        SimpleDateFormat sdfx = new java.text.SimpleDateFormat("hh:mm a");
        Date date = new java.util.Date(unix * 1000L);
        holder.time.setText(sdfx.format(date));

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        Calendar c2 = Calendar.getInstance();
        if (c2.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).equals(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())))
            holder.day.setText("Today");
        else
            holder.day.setText(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public Filter getFilter() {
        return null;
    }


    class Viewholder extends RecyclerView.ViewHolder {

        private TextView day, time, temp, desc;
        private ImageView icon;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.Day);
            time = itemView.findViewById(R.id.Time);
            temp = itemView.findViewById(R.id.Description);
            desc = itemView.findViewById(R.id.HourlyDescription);
            icon = itemView.findViewById(R.id.imageView2);
        }
    }

}
