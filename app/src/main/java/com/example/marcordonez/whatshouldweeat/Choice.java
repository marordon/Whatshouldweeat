package com.example.marcordonez.whatshouldweeat;

import java.util.Random;

/**
 * Created by marcordonez on 4/22/17.
 */

public class Choice {
    public String name;
    public String lat;
    public String lng;
    public String rateing;
    public String adress;
    public String imgurl;
    public String ftype;


    public Choice() {
        name="unavailable";
        lat="unavailable";
        lng="unavailable";
        rateing="unavailable";
        adress="unavailable";
        imgurl="unavailable";
        ftype="unavailable";


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

    public String getRateing() {
        return rateing;
    }

    public void setRateing(String rateing) {
        this.rateing = rateing;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }
}
