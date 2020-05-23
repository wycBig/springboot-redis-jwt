package com.wyc.springbootredis.mapper;

import com.wyc.springbootredis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> queryAll();
    @Select("select * from user where uid = #{id}")
    User findUserById(@Param("id") int id);
    @Select("select * from user where uname = #{uName}")
    User login(@Param("uName") String uName );
}
