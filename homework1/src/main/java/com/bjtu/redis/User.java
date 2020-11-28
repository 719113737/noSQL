package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class User {
    private String id;
    private int counter;
    private String des;
    private String act;
    private String time;

    public User() {
    }


    public void setID(String s) {
        this.id = s;
    }

    public String getID() {
        return this.id;
    }

    public void setCounter(int c) {
        this.counter = c;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setSTR(String s) {
        this.des = s;
    }

    public String getdes() {
        return this.des;
    }

    public void setAction(String a) {
        this.act = a;
    }

    public String getAction(){
        return this.act;
    }

    public void setTime(String t) {
        this.time = t;
    }

    public String getTime() {
        return this.time;
    }
}
