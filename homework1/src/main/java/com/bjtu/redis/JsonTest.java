package com.bjtu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonTest {
    private String LogPath;

    public JsonTest() {
        this.LogPath = ReadJson.class.getClassLoader().getResource("log.json").getPath();
    }

    public void load() {
        System.out.println("开始载入数据");
        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.flushDB();

        ReadJson reader = new ReadJson(this.LogPath);
        JSONObject jobj = reader.getJOBJ();

        Iterator iterator = jobj.keySet().iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject helper_obj = jobj.getJSONObject(key);
            increase("NUM",helper_obj.getIntValue("Num"));
            System.out.println("正在载入: "+key+" 次数: "+ helper_obj.getIntValue("Num") + "当前总点击量:"+ getInt());

            setLIST("List",key);
            setSET("SET",key);
            setZSET("ZSET",key);

            //setHash(key,"selfNum",String.valueOf(helper_obj.getIntValue("Num")));
            jedis.hincrBy(key,"selfNum",helper_obj.getIntValue("Num"));
            setHash(key,"Action",helper_obj.getString("Action"));
            setHash(key,"time",helper_obj.getString("time"));
            setHash(key,"description",helper_obj.getString("description"));
        }
        System.out.println("载入数据成功");
    }

    public void save() {
        System.out.println("开始保存记录");
        //put data in the json obj from redis
        JSONObject jobj = new JSONObject();
        //int num = Integer.parseInt(getInt());
        //jobj.put("NUM",num);
        //jobj.put("LIST",JSON.toJSON(getLIST(num)));
        //jobj.put("SET",JSON.toJSON(getSET()));
        //jobj.put("ZSET",JSON.toJSON(getZSET(num)));
        Iterator iterator = getSET("SET").iterator();
        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject helper = new JSONObject();
            helper.put("Num",Integer.valueOf(getHashStr(key,"selfNum")));
            helper.put("description",getHashStr(key,"description"));
            helper.put("Action",getHashStr(key,"Action"));
            helper.put("time",getHashStr(key,"time"));

            jobj.put(key,helper);
        }

        //write into log
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(this.LogPath));
            //System.out.println(jobj.toJSONString());
            out.write(jobj.toJSONString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //clear up
        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.flushDB();
        System.out.println("保存成功");
    }

    public void action(User user) {
        Jedis jedis = JedisInstance.getInstance().getResource();

        int counter = user.getCounter();
        String ID = user.getID();
        String des = user.getdes();
        String actiont = user.getAction();
        String time = user.getTime();


        //update the frequent
        getDate getD = new getDate();
        time = getD.updateTime(time);

        System.out.println("ID: " + ID + " 操作: " + actiont + " 点击数: "+counter + " 上次操作周期: " + getD.getold(time));

        increase("NUM",counter);
        setLIST("LIST",ID);
        setSET("SET",ID);
        setZSET("ZSET",ID);
        //setSTR(ID,des);

        setHash(ID,"description",des);
        setHash(ID,"Action",actiont);
        setHash(ID,"time",time);
        //setHash(ID,"selfNum",String.valueOf(counter));
        jedis.hincrBy(ID,"selfNum",counter);
    }

    //NUM的增加
    public void increase(String NUM,int count) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        for(int i = 0;i<count;i++) {
            jedis.incr(NUM);
        }
    }

    //添加STR
    public void setSTR(String ID,String STR) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(jedis.exists(ID))return;

        jedis.set(ID,STR);

    }

    public void setLIST(String list,String s) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.lpush(list,s);
    }

    public void setSET(String set,String s) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.sadd(set,s);
    }

    public void setZSET(String zset,String s) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        jedis.zadd(zset,1,s);
    }

    public void setHash(String hash,String key,String value) {
        Jedis jedis = JedisInstance.getInstance().getResource();

        //String tmp = jedis.hget(hash,key);
        jedis.hset(hash,key,value);
    }

    public String getInt() {
        Jedis jedis = JedisInstance.getInstance().getResource();
        return jedis.get("NUM");
    }

    public String getSTR(String key) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(key))return  "key:"+key+"不存在";
        return jedis.get(key);
    }

    public List<String>getLIST(String l,int range) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(l))return null;

        List<String> list = jedis.lrange(l,0,range);
        return list;
    }

    public Set<String>getSET(String s) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(s))return null;

        Set<String> set = jedis.smembers(s);
        return set;
    }

    public Set<String>getZSET(String z,int range) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(z))return null;

        Set<String> zset = jedis.zrange(z,0,range);
        return zset;
    }

    /*public int getHashNum(String hash,String key) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(hash) || !jedis.hexists(hash,key))return -1;

        return Integer.valueOf(jedis.hget(hash,key));
    }*/

    public String getHashStr(String hash,String key) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(hash) || !jedis.hexists(hash,key))return hash+"不存在或者"+key+"不存在";

        return jedis.hget(hash,key);
    }

    public String getFreq(String key) {
        Jedis jedis = JedisInstance.getInstance().getResource();
        if(!jedis.exists(key))return "未能找到"+key+"登录记录";

        getDate ge = new getDate();
        return key + "上次登录时间:" + ge.getNew(jedis.hget(key,"time")) ;
    }

}
