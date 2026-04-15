package com.qgtest.diary.mapper;

import com.qgtest.diary.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user(username,account,password,email,phone,motto,avatar) values (#{username},#{account},#{password},#{email},#{phone},#{motto},#{avatar})")
    //@Options(useGeneratedKeys = true, keyProperty = "id") 回填id到user表中，只有在注册完之后需要立刻使用id的时候再用，这里先注释掉，之后要用就取消注释
    int insert (User user);
    //会用到修改的参数是密码，座右铭和头像（手机号和邮箱不确定先把参数整上去再说）
    @Update("UPDATE user SET password = #{password},motto = #{motto},avatar = #{avatar},email = #{email},phone = #{phone} ,update_time = NOW() WHERE id = #{id}")
    int update(User user);
    @Select("select * from user where id=#{id}")
    User selectById(long id);
    @Select("select * from user where account = #{account}")
    User selectByAccount(String account);
    //精确搜索用户
    @Select("select * from user where email = #{email}")
    User selectDerectByEmail(String email);
    @Select("select * from user where phone = #{phone}")
    User selectDrectByPhone(String phone);
    //模糊搜索用户
    @Select("select * from user where (email like concat('%', #{keyword}, '%') or phone like concat('%', #{keyword}, '%'))")
    List<User> searchUsers(String keyword);
    @Select("select * from user order by create_time desc ")
    List<User> selectAllUser();
}
