package com.qgtest.diary.mapper;

import com.qgtest.diary.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user(username,account,password,email,phone,motto,avatar) values (#{username},#{account},#{password},#{email},#{phone},#{motto},#{avatar})")
    //@Options(useGeneratedKeys = true, keyProperty = "id") 回填id到user表中，只有在注册完之后需要立刻使用id的时候再用，这里先注释掉，之后要用就取消注释
    int insert (User user);
    @Update("")
    int update(User user);
    @Select("select * from user where id=#{id}")
    User selectById(long id);
    User selectByAccount(String account);
    User selectByKeyWord(String keyword);
    List<User> selectAllUser();

}
