package com.meeting.appo.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableCaching
public class RedisConfig extends JCacheConfigurerSupport {


    /**
     * 从yml中取得属性值并注入
     */
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    /**
     * 配置连接工厂
     * @return 配置好的连接工厂
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost,redisPort);
        return new JedisConnectionFactory(configuration);
    }


    /**
     * redis序列化器
     * RedisTemplate默认序列化器是JdkSerializationRedisSerializer,若用它来序列化的话
     * 被序列化的对象必须实现Serializable接口,在储存数据同时还保存了其他内容,总长度太长不易阅读
     * Jackson2JsonRedisSerializer 和GenericJackson2JsonRedisSerializer两者都能序列化为json,
     * 后者会在json后加入@class信息,属性,包路径,全类名等,如果存放的是list,而且在反序列化时没指定TypeReference 就会报错
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory factory){
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        RedisSerializer stringRedisSerializer = new StringRedisSerializer();

        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域,field,get,set,以及修饰符范围ANY包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入类型,必须是非final类型,否则异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //定义template并设置连接工程
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();

        //key的序列化用StringRedisSerializer
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //value使用GenericJackson2JsonRedisSerializer
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        //设置连接工厂并返回
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }


    @Bean
    public CacheManager initRedisCacheManager(RedisConnectionFactory factory){
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(factory);
        return builder.build();
    }
}
