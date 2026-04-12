package com.qgtest.diary.service;

import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.Result;
import com.qgtest.diary.entity.User;
import com.qgtest.diary.mapper.FriendshipMapper;
import com.qgtest.diary.mapper.NoteMapper;
import com.qgtest.diary.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.spec.ECField;
import java.text.Format;
import java.util.regex.Pattern;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NoteMapper noteMapper;
    @Autowired
    FriendshipMapper friendshipMapper;
    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    //加密工具，"SHA-256"的256位存储
    public static class EncryptUtil{
        public static String encrypt(String input){
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(input.getBytes());
                StringBuilder hex = new StringBuilder();
                for(byte b : hash){
                    hex.append(String.format("%02x",b));
                }
                return hex.toString();
            }catch (Exception e){
                return input;
            }
        }
        public static boolean check(String pwd,String encrypted){return encrypt(pwd).equals(encrypted);}
    }
    //登录、在controller中返回token
    public Result<User> login(String keyword,String password){
        if(keyword == null || keyword.isEmpty()){
            throw new BizException(401,"账号问空");
        }
        User user = new User();
        //查找用户
        if(keyword.contains("@")){
            user = userMapper.selectByEmail(keyword);
            if(user == null){throw new BizException(401,"用户未注册");}
        }else{
            user = userMapper.selectByPhone(keyword);
            if(user == null){throw new BizException(401,"用户未注册");}
        }
        //密码校验
        if(!EncryptUtil.check(password,user.getPassword())){
            throw new BizException(401,"密码错误");
        }
        return  Result.success(user);
    }
    //注册
    public void register( String account , String email,String phone,String password,String confirmPwd){
        if(email!=null || !email.isEmpty()){//邮箱校验
            if(!EMAIL.matcher(email).matches()){
                throw new BizException(401,"邮箱格式错误");
            }
            User existUser = userMapper.selectByEmail(email);
            if(existUser !=null){
                throw new BizException("邮箱已被注册");
            }
        }else{
            throw new BizException(401,"邮箱不能为空");
        }
        if(phone!=null || !phone.isEmpty()){//邮箱校验
            User existUser = userMapper.selectByPhone(phone);
            if(existUser !=null){
                throw new BizException("手机号已被注册");
            }
        }else{
            throw new BizException(401,"手机号不能为空");
        }
        if(account == null || account.isEmpty()){throw new BizException(401,"账号不能为空");}
        if(!password.equals(confirmPwd)){throw new BizException(401,"确认密码错误");}
        String encrypted = EncryptUtil.encrypt(password);
        User user = new User();
        user.setAccount(account);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(encrypted);
        userMapper.insert(user);
    }
}
