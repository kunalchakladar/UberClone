package com.example.kunal.uberclone;

/**
 * Created by Kunal on 8/1/2017.
 */

public class Driver {

    Double lat, lng;
    Boolean available;

    public Driver() {
    }

    public Driver(Double lat, Double lng, Boolean available) {
        this.lat = lat;
        this.lng = lng;
        this.available = available;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
