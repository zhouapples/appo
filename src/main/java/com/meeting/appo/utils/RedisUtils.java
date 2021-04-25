package com.meeting.appo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    //字符串缓存模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //对象缓存模板
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 重置过期时间
     * @param key
     * @param seconds   过期时间
     */
    public void reset(String key,Long seconds){
        stringRedisTemplate.expire(key,seconds, TimeUnit.SECONDS);
    }

    /**
     * 批量删除
     */
    public void delKeys(String pattern){
        redisTemplate.delete(stringRedisTemplate.keys(pattern));
    }

    /**
     * 添加set集合
     * @param key
     * @param set
     */
    public void addSet(String key, Set<?> set){
        try {
            redisTemplate.opsForSet().add(key,set);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取set
     * @param key
     * @return
     */
    public Set<?> getSet(String key){
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加字符串数据
     * @param key
     * @param value
     * @param sec
     */
    public void addString(String key,String value,Long sec){
        try {
            ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
            if (sec!=null){
                valueOperations.set(key,value,sec,TimeUnit.SECONDS);
            }else{
                valueOperations.set(key,value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public String getString(String key){
        String res = "";
         try {
             res = stringRedisTemplate.opsForValue().get(key);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return res;
     }


    public void delString(String key){
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delAllString(String key){
        if (key==null || key.equals("")){
            //若key不存在则终止
            return;
        }
        try {
            if (!key.endsWith("*")){
                key += "*";
            }
            Set<String> keys = stringRedisTemplate.keys(key);
            Iterator<String> it = keys.iterator();
            //递归删除
            while (it.hasNext()){
                String singleKey = it.next();
                delString(singleKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 对象缓存
     * @param key
     * @param obj
     * @param sec
     */
    public void addObj(String key,Object obj,Long sec){
        try {
            //对象储存
            ValueOperations<Object,Object> objOps = redisTemplate.opsForValue();
            if (sec!=null){
                objOps.set(key,obj,sec,TimeUnit.SECONDS);
            }else{
                objOps.set(key,obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getObject(String key){
        Object object = null;
        try {
            object = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 取缓存数据
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public <T> T getObj(String key, T t){
        Object o = null;
        try {
            o = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o != null ? (T)o : null;
    }

    /**
     * 设置过期时间
     * @param key
     * @param sec
     */
    public void expire(String key,long sec){
        try {
            stringRedisTemplate.expire(key,sec,TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delObj(String key){
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 压栈,push list-->redis
     * @param key
     * @param value
     * @return
     */
    public Long push(String key,String value){
        Long res = 0l;
        try {
            res = stringRedisTemplate.opsForList().leftPush(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 出栈 redis --> String
     * @param key
     * @return
     */
    public String pop(String key){
        String popres = "";
        try {
            popres = stringRedisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  popres;
    }

    /**
     * 入队 list --> rightpush
     * @param key
     * @param value
     * @return
     */
    public Long in(String key,String value){
        Long inres = 0l;
        try {
            inres = stringRedisTemplate.opsForList().rightPush(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inres;
    }

    public String out(String key){
        String outres = "";
        try {
            outres = stringRedisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outres;
    }

    /**
     * 队列/栈 长度
     * @param key
     * @return
     */
    public Long length(String key){
        Long lengRes = 0l;
        try {
            lengRes = stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lengRes;
    }

    /**
     * 范围检索,局部查找
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> range(String key, int start, int end){
        List<String> rangeRes = null;
        rangeRes = stringRedisTemplate.opsForList().range(key,start,end);
        return rangeRes;
    }

    /**
     * 移除
     * @param key
     * @param i
     * @param value
     */
    public void remove(String key,long i,String value){
        try {
            stringRedisTemplate.opsForList().remove(key,i,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String index(String key,long index){
        String indexRes = "";
        try {
            indexRes = stringRedisTemplate.opsForList().index(key,index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return indexRes;
    }



    /**
     * 设置值
     * @param key
     * @param value
     * @param index
     */
    public void setObject(String key,Object value,long index){
        try {
            redisTemplate.opsForValue().set(key,value,index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setString(String key,String value){
        try {
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * list裁剪
     * @param key
     * @param start
     * @param end
     */
    public void trim(String key,long start,int end){
        try {
            stringRedisTemplate.opsForList().trim(key,start,end);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 原子性自增
     *
     * @param key   自增的key
     * @param value 每次自增的值
     * @return Long
     */
    public Long incr(String key,long value){
        Long incrRes = 0l;
        try {
            incrRes = stringRedisTemplate.opsForValue().increment(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incrRes;
    }

    private String spellString(String err,Object ... args){
        return MessageFormat.format(err,args);
    }


    public boolean hasKey(String key){
        return stringRedisTemplate.hasKey(key)!=null;
    }





}
