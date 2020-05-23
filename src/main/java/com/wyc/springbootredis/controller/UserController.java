package com.wyc.springbootredis.controller;

import com.wyc.springbootredis.entity.User;
import com.wyc.springbootredis.service.TokenService;
import com.wyc.springbootredis.service.UserService;
import com.wyc.springbootredis.util.JwtUtils;
import com.wyc.springbootredis.util.UserLoginToken;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping("/queryAll")
    public List<User> queryAll(){
        List<User> lists = userService.queryAll();
        return lists;
    }

    @UserLoginToken
    @RequestMapping("/findUserById")
    public Map<String, Object> findUserById(@RequestParam int id){
        User user = userService.findUserById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("uid", user.getUid());
        result.put("uname", user.getuName());
        result.put("pass", user.getPassword());
        return result;
    }
    @RequestMapping("/login")
    public Map<String,Object> login(User user, HttpServletResponse response){
       Map<String,Object> map = new HashMap<>();
       //JSONObject object = new JSONObject();
        User user1 = userService.login(user);
        if (user1 == null){
            map.put("message","登陆失败，用户不存在！");
            return map;
        }else{
            if(!user1.getPassword().equals(user.getPassword())){
                map.put("message","登陆失败，密码错误！");
                return map;
            }else{
                String token =tokenService.getToken(user1);
                map.put("token",token);
                map.put("user",user1);
                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                response.addCookie(cookie);
                return map;
            }
        }
    }
    @UserLoginToken
    @GetMapping("/getMessage")
    public String getMessage(){
        // 取出token中带的用户id 进行操作
        System.out.println(JwtUtils.getTokenUserId());
        return "您已通过验证";
    }
}
