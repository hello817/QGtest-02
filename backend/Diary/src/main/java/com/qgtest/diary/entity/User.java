package com.qgtest.diary.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private long id;
    private String username;
    private String account;
    private String password;
    private String emil;
    private String phone;
    private String motto;
    private String avatar;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
