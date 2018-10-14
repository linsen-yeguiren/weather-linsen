package com.example.linsen.weather.bean;

/**
 * Created by linsen on 2018/10/10.
 */

public final class TodayWeather {
    private String city;
    private String updatetime;
    private String temp;
    private String humid;
    private String pm25;
    private String quality;
    private String winderection;
    private String windpow;
    private String date;
    private String high;
    private String low;
    private String type;

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getHumid() {
        return humid;
    }

    public void setHumid(String humid) {
        this.humid = humid;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWinderection() {
        return winderection;
    }

    public void setWinderection(String winderection) {
        this.winderection = winderection;
    }

    public String getWindpow() {
        return windpow;
    }

    public void setWindpow(String windpow) {
        this.windpow = windpow;
    }

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", temp='" + temp + '\'' +
                ", humid='" + humid + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", quality='" + quality + '\'' +
                ", winderection='" + winderection + '\'' +
                ", windpow='" + windpow + '\'' +
                ", data='" + date + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
