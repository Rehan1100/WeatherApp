package com.example.weatherapp.ForcastDaily;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;

public class ForcastDailyAdapter extends RecyclerView.Adapter<ForcastDailyAdapter.Viewholder> implements Filterable {

    Context context;

    public ForcastDailyAdapter(Context context) {
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    @Override
    public Filter getFilter() {
        return null;
    }


    class Viewholder extends RecyclerView.ViewHolder {


        public Viewholder(@NonNull View itemView) {
            super(itemView);


        }


        private void setData() {


        }
    }

}
