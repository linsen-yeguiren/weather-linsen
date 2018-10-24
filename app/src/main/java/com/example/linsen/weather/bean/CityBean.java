package com.example.linsen.weather.bean;

/**
 * Created by linsen on 2018/10/20.
 */

public class CityBean {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFristPY;

    public CityBean(String province,String city,String number,String firstPY,String allPY,String allFristPY) {
        this.allFristPY = allFristPY;
        this.allPY = allPY;
        this.city = city;
        this.firstPY = firstPY;
        this.number = number;
        this.province = province;
    }

    public String getAllFristPY() {
        return allFristPY;
    }

    public void setAllFristPY(String allFristPY) {
        this.allFristPY = allFristPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
