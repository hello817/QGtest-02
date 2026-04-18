package com.qgtest.diary.dto.userDTO;

import lombok.Data;

@Data
public class UserInfoVO {
    private Long userId;
    private String username;
    private String account;
    private String email;
    private String phone;
    private String avatar;
    private String motto;
}
