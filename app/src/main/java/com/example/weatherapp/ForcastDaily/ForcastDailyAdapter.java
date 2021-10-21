package com.example.weatherapp.ForcastDaily;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Api.Classes.Daily;
import com.example.weatherapp.R;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForcastDailyAdapter extends RecyclerView.Adapter<ForcastDailyAdapter.Viewholder> implements Filterable {

    Context context;
    List<Daily> data;
    String tempunit;

    public ForcastDailyAdapter(List<Daily> daily, Context context) {
        this.context = context;
        this.data = daily;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_settings",MODE_PRIVATE);
        tempunit = sharedPreferences.getString("tempunit","F");
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_forcast_weather_row
                , parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForcastDailyAdapter.Viewholder holder, int position) {

        long unix = data.get(position).dt;
        SimpleDateFormat sdfx = new java.text.SimpleDateFormat("EEEE , dd / MM");
        Date date = new java.util.Date(unix * 1000L);
        holder.date.setText(sdfx.format(date));

        int tempmax = 0;
        int tempmin = 0;
        int tempm = 0, tempa = 0,tempe = 0,tempn = 0;
        if(tempunit.equals("F")){
            tempmax = convertoF(data.get(position).temp.max);
            tempmin = convertoF(data.get(position).temp.min);
            tempm = convertoF(data.get(position).temp.morn);
            tempa = convertoF(data.get(position).temp.day);
            tempe = convertoF(data.get(position).temp.eve);
            tempn = convertoF(data.get(position).temp.night);

            holder.temp.setText(tempmax+"°F/"+tempmin+"°F");
            holder.temp_a.setText(tempa+"°F");
            holder.temp_m.setText(tempm+"°F");
            holder.temp_e.setText(tempe+"°F");
            holder.temp_n.setText(tempn+"°F");
        }
        else if(tempunit.equals("C")){
            tempmax = convertoC(data.get(position).temp.max);
            tempmin = convertoC(data.get(position).temp.min);
            tempm = convertoC(data.get(position).temp.morn);
            tempa = convertoC(data.get(position).temp.day);
            tempe = convertoC(data.get(position).temp.eve);
            tempn = convertoC(data.get(position).temp.night);

            holder.temp.setText(tempmax+"°C/"+tempmin+"°C");
            holder.temp_a.setText(tempa+"°C");
            holder.temp_m.setText(tempm+"°C");
            holder.temp_e.setText(tempe+"°C");
            holder.temp_n.setText(tempn+"°C");

        }
        holder.desc.setText(data.get(position).weather.get(0).description);

        float rain = data.get(position).pop;
        try{
            holder.rain.setText("("+rain+"% precip.)");
        }catch (Exception e){
            holder.rain.setText("(0% precip.)");
        }

        holder.uv.setText("UV Index: "+data.get(position).uvi);
        String iconCode = "_" + data.get(position).weather.get(0).icon;
        int iconResId = context.getResources().getIdentifier(iconCode,
                "drawable", context.getPackageName());
        holder.icon.setImageResource(iconResId);


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

        TextView date,temp,desc,rain,uv,temp_m,temp_a,temp_e,temp_n;
        ImageView icon;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.dayanddate);
            temp = itemView.findViewById(R.id.highlowtemprature);
            desc = itemView.findViewById(R.id.Description);
            rain = itemView.findViewById(R.id.Probability);
            uv = itemView.findViewById(R.id.UvIndex);
            temp_m = itemView.findViewById(R.id.morningtemprature);
            temp_a = itemView.findViewById(R.id.daytimeTemp);
            temp_e = itemView.findViewById(R.id.eveningTemprature);
            temp_n = itemView.findViewById(R.id.NightTemprature);
            icon = itemView.findViewById(R.id.weathericon);

        }

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
