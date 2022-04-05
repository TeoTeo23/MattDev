package com.example.mattdev;

public class Place {
    private double latitude;
    private double longitude;

    public Place(double lat, double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude(){ return this.latitude; }
    public double getLongitude(){ return this.longitude; }
}
