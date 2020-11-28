package com.bjtu.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {

    public static void main(String args[]) {

        //获得代表用户操作的json文件
        ReadJson reader = new ReadJson(ReadJson.class.getClassLoader().getResource("test.json").getPath());
        JsonTest test = new JsonTest();

        //加载记录
        test.load();

        //对读取json文件内容操作,对json内每个用户信息转为user实体，并返回文件中的所有用户的数据
        List<User>users = reader.getUsers();

        //一个action包括Num的增加，list、set、zset的增加
        for(User u : users) {
            test.action(u);
        }

        //查找用户登录周期
        System.out.println(test.getFreq("user1"));
        System.out.println(test.getFreq("user100"));

        //incr测试
        System.out.println("当前系统点击数:" + test.getInt());
        test.increase("NUM",10);
        System.out.println("当前系统点击数:" + test.getInt());

        //STR测试
        test.setSTR("test","test-STR");
        System.out.println(test.getSTR("test"));


        //获得LIST SET ZSET
        System.out.println(test.getLIST("LIST",10));
        System.out.println(test.getSET("SET"));
        System.out.println(test.getZSET("ZSET",10));


        //保存记录，保存至 target/classes/com/log.json
        test.save();


    }
}
