package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@TableName("friendshsip")
public class Friendship {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private long friendId;
    private Integer state;
    private LocalDateTime creatTime;

    public Friendship(){};
    public Friendship(long user_id,long friend_id){
        this.userId = user_id;
        this.friendId = friend_id;
        this.creatTime = LocalDateTime.now();
    }
}
