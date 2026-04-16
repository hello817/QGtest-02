package com.qgtest.diary.controller;

import com.qgtest.diary.common.Result;
import com.qgtest.diary.dto.userDTO.*;
import com.qgtest.diary.entity.User;
import com.qgtest.diary.service.UserService;
import com.qgtest.diary.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired private JwtUtils jwtUtils;  // JWT工具
    //注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(dto.getAccount(), dto.getEmail(), dto.getPhone(),
                dto.getPassword(), dto.getConfirmPassword());
        return Result.success();
    }
    //登录--这里要用值对象只返回必要数据，以免造成安全风险
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO dto) {
        User user = userService.login(dto.getKeyword(), dto.getPassword());
        String token = jwtUtils.generateToken(user.getId(), user.getAccount());
        LoginVO vo = new LoginVO(token, user.getId(), user.getAccount(), user.getUsername(), user.getAvatar());
        return Result.success(vo);
    }
    //修改密码
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody @Valid UpdatePasswordDTO dto,
                                       @RequestAttribute Long userId) {
        userService.resetPassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return Result.success();
    }
    //修改座右铭
    @PutMapping("/motto")
    public Result<Void> updateMotto(@RequestBody @Valid UpdateMottoDTO dto,
                                    @RequestAttribute Long userId) {
        userService.resetMotto(dto.getMotto(), userId);
        return Result.success();
    }
    //修改头像
    @PutMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam("file") MultipartFile file,
                                       @RequestAttribute Long userId) {
        String avatarUrl = userService.updateAvatar(userId, file);
        return Result.success(avatarUrl);
    }
    //查找用户
    @GetMapping("/search")
    public Result<List<User>> searchUsers(@RequestParam String keyword) {
        return Result.success(userService.findUserByKeyWord(keyword));
    }
    //===================================================================================
    //再想到什么写下面吧
}
