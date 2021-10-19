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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.example.weatherapp.R;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.Viewholder> implements Filterable {

    Context context;

    public HourlyWeatherAdapter( Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.front_page_row_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherAdapter.Viewholder holder, int position) {

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

        private TextView title,desc,deadlinedate,startdate;
        private ImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


        }


        private void setData() {


        }
    }

}
