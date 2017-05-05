package com.example.marcordonez.whatshouldweeat;

/**
 * Created by marcordonez on 4/22/17.
 */

public class Choice {

    String name;
    String lat;
    String lng;
    String rating;
    String address;
    String imgurl;
    DatabaseHelper.FType ftype;



    public Choice() {
        name="unavailable";
        lat="unavailable";
        lng="unavailable";
        rating ="unavailable";
        address ="unavailable";
        imgurl="unavailable";
        ftype= DatabaseHelper.FType.OTHER;
    }

    public Choice(String name, String lat, String lng, String rating, String address, String imgurl, DatabaseHelper.FType ftype) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.address = address;
        this.imgurl = imgurl;
        this.ftype = ftype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public DatabaseHelper.FType getFtype() {
        return ftype;
    }

    public void setFtype(DatabaseHelper.FType ftype) {
        this.ftype = ftype;
    }

    @Override
    public String toString(){
        return "Choice: Name[" + getName() + "], Lat[" + getLat() + "], Long[" + getLng() + "], Rating[" + getRating() + "], Address[" + getAddress() + "], Imgurl[" + getImgurl() + "], Ftype[" + getFtype().getDisplayName() + "]";
    }
}
