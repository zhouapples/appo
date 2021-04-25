package com.meeting.appo.services;


import com.meeting.appo.dao.EventStatusDao;
import com.meeting.appo.entities.Dept;
import com.meeting.appo.entities.Status;
import com.meeting.appo.entities.User;
import com.meeting.appo.utils.Chinese2Base64Utils;
import com.meeting.appo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatusService {

    @Autowired
    private EventStatusDao statusDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;


    public List<Status> getStatusList(String day,String rid){
        String key = day+"_"+rid+"_statusList";

        //判断是否存在缓存
        boolean hasKey = redisTemplate.hasKey(key);
        List<Status> statusList = null;
        if(hasKey){
            //取出缓存数据
            statusList = (List<Status>)redisUtils.getObject(key);
            System.out.println("getStatusList命中缓存");
        }else{
            try {
                System.out.println("getStatusList查询数据库");
                statusList = statusDao.getStatusList(day, rid);
                //设置缓存
                if (statusList.size()!=0){
                    redisUtils.addObj(key,statusList,3600l);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //不要把 null return出去
        return statusList!=null?statusList:new ArrayList<>();

    }

    public Status getStatusBySid(int sid){

        //取缓存
        String key = "status_"+sid;
        ValueOperations<String,Status> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);
        Status status = null;
        if (hasKey){
            status = (Status)redisUtils.getObject(key);
        }else{
            status = statusDao.getStatusBySid(sid);
            //取不到,设置缓存
            redisUtils.addObj(key,status,3600l);
        }

        return status!=null?status:new Status();
    }

    public void rmStatusById(int sid,@Nullable int uid){
        //删除数据库数据
        statusDao.rmStatusById(sid);
        //更新缓存
        String key = "uid"+uid+"_30day_status";
        List<Status> statusList = (List<Status>)redisUtils.getObject(key);
        for (Status s:statusList){
            if (s.getSid()!=null&&s.getSid().equals(sid)){
                statusList.remove(s);
                break;
            }
        }

        //删除旧缓存,立刻过期
        redisUtils.expire(key,0l);
        //增加新缓存
        redisUtils.addObj(key,statusList,3600l);

    }

    public List<Dept> getAllDept(){
        String key = "depts_list";
        //判断是否存在缓存
        boolean hasKey = redisTemplate.hasKey(key);
        List<Dept> deptList = null;
        if(hasKey){
            //取出缓存数据
            deptList = (List<Dept>)redisUtils.getObject(key);
            System.out.println("deptList命中缓存");
        }else{
            deptList = statusDao.getAllDept();
            //设置缓存
            redisUtils.addObj(key,deptList,3600l);
        }

        //不要把 null return出去
        return deptList!=null?deptList:new ArrayList<>();

    }

    public User getUserByUsername(String username){
        String key = "user_"+ Chinese2Base64Utils.encodeBase64(username);

        //判断是否存在缓存
        boolean hasKey = redisTemplate.hasKey(key);
        User user = null;
        if(hasKey){
            //取出缓存数据
            user = (User) redisUtils.getObject(key);
        }else{
            user = statusDao.getUserByUsername(username);
            //设置缓存
            redisUtils.setObject(key,user,3600l);
        }

        //不要把 null return出去
        return user!=null?user:new User();
    }



    public int getStatusCount(String day){
        return statusDao.getStatusCount(day);
    }

    /**
     * 条形图显示统计
     * @return
     */
    public Map<String,Object> showHistogram(){
        String key = "Histogram";

        HashOperations<String,String,Object> operations = redisTemplate.opsForHash();
        //判断是否存在缓存
        boolean hasKey = redisTemplate.hasKey(key);
        Map<String,Object> detail = null;
        if (hasKey){
            detail = operations.entries(key);   //得到Map
            //value = operations.get(keyOfRedis,keyOfMap);   //得到Map里面的某个key的值
        }else{
            detail = statusDao.showHistogram();
            //设置缓存 -- hash
            operations.putAll(key,detail);
        }
        return detail;
    }

    public List<Status> adminStatusList(int start,int end){
        return statusDao.adminStatusList(start,end);
    }

    public void setStatusState(int sid,boolean state,int uid){
        statusDao.setStatusState(sid,state);
        //更新缓存
        String key = "uid"+uid+"_30day_status";
        List<Status> statusList = (List<Status>)redisUtils.getObject(key);
        for (Status s:statusList){
            if (s.getSid()!=null&&s.getSid().equals(sid)){
                s.setStatus(state);
                break;
            }
        }
        //删除旧缓存,立刻过期
        redisUtils.expire(key,0l);
        //增加新缓存
        redisUtils.addObj(key,statusList,3600l);
    }

    public List<Status> getStatusByUser(int uid,Long timestamp){
        String key = "uid"+uid+"_30day_status";
        boolean haskey = redisTemplate.hasKey(key);
        List<Status> statusList;
        if (haskey){
            statusList = (List<Status>)redisUtils.getObject(key);
        }else{
            //缓存每个人30天内的预约数据

            statusList = statusDao.getStatusByUser(uid,timestamp);
            if(statusList.size()!=0){
                redisUtils.addObj(key,statusList,3600l);
            }
        }
        return statusList;
    }

    public void addStatus(Status status){
        //先删缓存再写数据库
        String key1 = new SimpleDateFormat("yyyy-MM-dd").format(status.getStart_date()) + "_" + status.getRid() + "_statusList";
        String key2 = new SimpleDateFormat("yyyy-MM-dd").format(status.getStart_date()) + "_null_statusList";
        redisUtils.delObj(key1);
        redisUtils.delObj(key2);
        statusDao.addStatus(status);
        //添加到数据库后写入redis
        redisUtils.setObject(key1,status,3600l);
    }
}
