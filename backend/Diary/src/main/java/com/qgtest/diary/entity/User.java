package com.qgtest.diary.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private long id;
    private String username;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String motto;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public User(){}
    public User(String username,String account,String password,String email,String phone){
        this.username = username;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

}
