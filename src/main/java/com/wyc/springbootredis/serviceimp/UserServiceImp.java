package com.wyc.springbootredis.serviceimp;

import com.wyc.springbootredis.entity.User;
import com.wyc.springbootredis.mapper.UserMapper;
import com.wyc.springbootredis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImp  implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<User> queryAll() {
       return userMapper.queryAll();
    }

    @Override
    public User findUserById(int id) {

        String key = "user_" + id;
        //操作字符串
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        //判断redis中是否有键为key的缓存
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            System.out.println("从缓存中获得数据："+user.getuName());
            System.out.println("------------------------------------");
            return user;
        } else {
            User user = userMapper.findUserById(id);
            System.out.println("查询数据库获得数据："+user.getuName());
            System.out.println("------------------------------------");

            // 写入缓存
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    @Override
    public User login(User user) {
        return userMapper.login(user.getuName());
    }
}
