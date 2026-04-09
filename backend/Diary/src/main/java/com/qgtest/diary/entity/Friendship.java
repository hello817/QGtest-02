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
    private long user_id;
    private long friend_id;
    private Integer state;
    private LocalDateTime creatTime;

    public Friendship(){};
    public Friendship(long user_id,long friend_id){
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.creatTime = LocalDateTime.now();
    }
}
