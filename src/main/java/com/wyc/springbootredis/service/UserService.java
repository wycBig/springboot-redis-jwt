package com.wyc.springbootredis.service;

import com.wyc.springbootredis.entity.User;

import java.util.List;

public interface UserService {
    List<User> queryAll();

    User findUserById(int id);

    User login(User user);
}
