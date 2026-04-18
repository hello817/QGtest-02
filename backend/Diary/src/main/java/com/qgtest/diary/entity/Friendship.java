package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qgtest.diary.common.enums.FriendshipStatus;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
@TableName("friendshsip")
public class Friendship {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
    private String groupTag;
    private LocalDateTime creatTime;

    public Friendship(){};
    public Friendship(Long userId, Long friendId){
        this.userId = userId;
        this.friendId = friendId;
        this.creatTime = LocalDateTime.now();
    }
    public Friendship(Long userId, Long friendId,String groupTag){
        this.userId = userId;
        this.friendId = friendId;
        this.creatTime = LocalDateTime.now();
        this.groupTag = groupTag !=null?groupTag:"默认";
    }
}
