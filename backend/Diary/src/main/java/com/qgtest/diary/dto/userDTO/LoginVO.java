package com.qgtest.diary.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginVO {
    @NotBlank
    private String token;
    @NotBlank
    private Long userId;
    private String account;
    private String username;
    private String avatar;

    public LoginVO(String token , Long userId , String account , String username , String avatar){
        this.token = token;
        this.userId = userId;
        this.account = account;
        this.username = username;
        this.avatar = avatar;
    }
}
