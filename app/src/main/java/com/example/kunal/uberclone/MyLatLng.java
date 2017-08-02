package com.example.kunal.uberclone;

/**
 * Created by Kunal on 8/3/2017.
 */

public class MyLatLng {

    Double latitude;
    Double longitude;

    public MyLatLng() {
    }

    public MyLatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
