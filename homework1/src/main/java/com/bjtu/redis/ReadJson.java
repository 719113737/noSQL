package com.bjtu.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadJson {

    private String path;
    private String JsonStr;
    private JSONObject jobj;


    public ReadJson(String path) {
        try {
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            this.JsonStr = sb.toString();

            this.jobj = JSON.parseObject(this.JsonStr);


        } catch(IOException e) {
            e.printStackTrace();
        }
        this.path = path;
    }

    public String getStr() {
        return this.JsonStr;
    }

    public JSONObject getJOBJ() {
        return this.jobj;
    }

    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        Iterator iterator = this.jobj.keySet().iterator();
        while(iterator.hasNext()) {
            User user = new User();
            String key = (String) iterator.next();
            JSONObject helper = this.jobj.getJSONObject(key);
            user.setID(key);
            user.setCounter(helper.getIntValue("Num"));
            user.setSTR(helper.getString("description"));
            user.setAction(helper.getString("Action"));
            user.setTime("");

            users.add(user);
        }

        return users;
    }

    /*----------------------------------test------------------------------------*/
    public static void main(String args[]) {
        /*
        String path = ReadJson.class.getClassLoader().getResource("test.json").getPath();
        ReadJson r = new ReadJson(path);
        */
        /*
        ArrayList<String> list = new ArrayList<String>();
        list.add("aaaa");
        list.add("bbbb");

        JSONObject jobj = new JSONObject();
        jobj.put("LIST",JSON.toJSON(list));
        System.out.println(jobj);
       /* JSONArray list = jobj.getJSONArray("action");
        for(int i = 0;i<list.size();i++) {
            JSONObject obj = (JSONObject) list.get(i);

            System.out.println(obj.get("id"));
            System.out.println(obj.getInteger("counter"));
        }*/
        String path = ReadJson.class.getClassLoader().getResource("log.json").getPath();
        ReadJson reader = new ReadJson(path);
        Iterator iterator = reader.jobj.keySet().iterator();
        while(iterator.hasNext()) {
            String helper = (String) iterator.next();
            JSONObject helper_obj = reader.jobj.getJSONObject(helper);
            System.out.println(helper_obj.getInteger("Num"));
            System.out.println(helper_obj.getString("description"));
            System.out.println(helper_obj.getString("Action"));
            System.out.println(helper_obj.getString("time"));
        }
    }
    /*----------------------------------test------------------------------------*/

}
