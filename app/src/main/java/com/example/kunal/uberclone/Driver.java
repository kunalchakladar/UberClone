package com.example.kunal.uberclone;

/**
 * Created by Kunal on 8/1/2017.
 */

public class Driver {

    String name;
    Double lat, lng;
    Long phnNum;
    Boolean available;

    public Driver() {
    }

    public Driver(String name, Double lat, Double lng, Long phnNum, Boolean available) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.phnNum = phnNum;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getPhnNum() {
        return phnNum;
    }

    public void setPhnNum(Long phnNum) {
        this.phnNum = phnNum;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
