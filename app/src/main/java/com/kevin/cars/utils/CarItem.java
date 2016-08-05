package com.kevin.cars.utils;

/**
 * Created by Administrator
 * on 2016/8/5.
 */
public class CarItem {

    String mac;
    String shortAddr;

    public CarItem(String mac,String shortAddr){
        this.mac = mac;
        this.shortAddr = shortAddr;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getShortAddr() {
        return shortAddr;
    }

    public void setShortAddr(String shortAddr) {
        this.shortAddr = shortAddr;
    }



}
