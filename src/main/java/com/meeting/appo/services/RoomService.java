package com.meeting.appo.services;

import com.meeting.appo.dao.EventRoomDao;
import com.meeting.appo.entities.Room;
import com.meeting.appo.entities.Status;
import com.meeting.appo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private EventRoomDao roomDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    public List<Room> queryAllRooms(){
        String key = "allRoomsList";
        //判断是否存在缓存
        boolean hasKey = redisTemplate.hasKey(key);
        List<Room> allRoomsList = null;
        if(hasKey){
            //取出缓存数据
            allRoomsList = (List<Room>)redisUtils.getObject(key);
            System.out.println("getStatusList命中缓存");
        }else{
            try {
                System.out.println("getStatusList查询数据库");
                allRoomsList = roomDao.queryAllRooms();
                //设置缓存
                if (allRoomsList.size()!=0){
                    redisUtils.addObj(key,allRoomsList,3600l);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allRoomsList;
    }

    public int getRoomCount(){
        return roomDao.getRoomCount();
    }

    public List<Room> queryAllRoomsByadmin(){
        return roomDao.queryAllRoomsByadmin();
    }

    public void modRoom(Room room){
        roomDao.modRoom(room);
    }

    public void addRoom(Room room){
        roomDao.addRoom(room);
    }

    public void rmRoom(int rid){
        roomDao.rmRoom(rid);
    }




}
