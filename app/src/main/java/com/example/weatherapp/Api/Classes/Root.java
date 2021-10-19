package com.example.weatherapp.Api.Classes;
import java.util.List; 
public class Root{
    public double lat;
    public double lon;
    public String timezone;
    public int timezone_offset;
    public Current current;
    public List<Hourly> hourly;
    public List<Daily> daily;
}
