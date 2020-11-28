package com.bjtu.redis;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class getDate {

    public getDate() {

    }

    public String getold(String str) {
        if(str == null || str.length()==0) {
            return str;
        }
        String[] s = str.split("\\s+");

        if(s.length <=1)return s[0];

        return s[1];
    }

    public String getNew(String str) {
        if(str == null || str.length()==0) {
            return str;
        }

        String[] s = str.split("\\s+");

        return s[0];
    }

    public String updateTime(String old) {
        String newstr = "";

        if(!old.equals("")) {
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            newstr =  df.format(day) + " " + getNew(old);
        }
        else {
            Date day=new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
            newstr =  df.format(day);
        }

        return newstr;
    }

    public static void main(String args[]) {
        getDate d = new getDate();
        System.out.println(d.updateTime("202011261934 202011252011"));

    }
}
