package com.meeting.appo.services;


import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.dao.EventUserDao;
import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.Chinese2Base64Utils;
import com.meeting.appo.utils.Chinese2PinyinUtils;
import com.meeting.appo.utils.SecEncodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private EventUserDao userDao;
    @Autowired
    private EventStatusDao statusDao;

    @Autowired
    private RedisTemplate redisTemplate;




    public Map<String,Object> regUser(User user){

        Map<String,Object> map = new HashMap();
        String pinyin_name = Chinese2PinyinUtils.toPinyin(user.getUsername());


        //准备从缓存取数据或者设置缓存
        String key = "user_"+Chinese2Base64Utils.encodeBase64(user.getUsername());

        ValueOperations<String,User> operations = redisTemplate.opsForValue();



        //判断是否存在该用户
        User existUser=null;
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey){
            existUser = operations.get(key);
            System.out.println("regUser-existUser 缓存命中");
        }else{
            existUser = statusDao.getUserByUsername(user.getUsername());
            //写入缓存
            operations.set(key,existUser,2, TimeUnit.HOURS);   //缓存有效期,12 hours
            System.out.println("regUser-existUser 查询数据库");
        }


        if(user.getUsername().equals("") || user.getMobile().equals("") || user.getPassword().equals("")){
            map.put("errMs","信息不完整");
            return map;
        }else if(user.getMobile().length() <11){
            map.put("errMs","手机号码长度有误");
            return map;
        }else if(existUser!=null || existUser.getPinyin_name().equals(pinyin_name)) {//不允许出现同名或同拼音名
            map.put("errMs","该用户名已被注册,请修改");
            return map;
        }else
        {   //验证通过

            //密码加密
            String sec_pwd = SecEncodeUtils.secEncode(user.getPassword());

            User newUser = new User(user.getUsername(),sec_pwd,user.getMobile(),user.getDeptId(),user.getCreate_date(),user.isAdmin(),pinyin_name);

            userDao.addUser(newUser);
        }

        return null;

    }


    public int getUserCount(){
        return userDao.getUserCount();
    }

    public List<User> queryAllUsers(){
        return userDao.queryAllUsers();
    }

    public void modUser(Map userMap){
        userDao.modUser(userMap);
    }

    public void modUser(User user){
        userDao.modUser(user);
    }

    public void rmUser(int uid){
        userDao.rmUser(uid);
    }

    public void resetPwd(String password, int uid){
        userDao.resetPwd(password,uid);
    }

    public void modDept(Dept dept){
        userDao.modDept(dept);
    }

    public void rmDept(int did){
        userDao.rmDept(did);
    }

    public User getUserById(int uid){
        return userDao.getUserById(uid);
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    public void addDept(Dept dept){
        userDao.addDept(dept);
    }

}
