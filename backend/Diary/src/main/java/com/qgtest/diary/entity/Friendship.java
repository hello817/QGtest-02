package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qgtest.diary.common.enums.FriendshipStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("friendship")
public class Friendship {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long friendId;
    @TableField("state")
    private FriendshipStatus status;
    private String groupTag;
    @TableField("create_time")
    private LocalDateTime createTime;

    public Friendship(){};
    public Friendship(Long userId, Long friendId){
        this.userId = userId;
        this.friendId = friendId;
        this.createTime = LocalDateTime.now();
        this.status = FriendshipStatus.WAITTING;
    }
    public Friendship(Long userId, Long friendId, String groupTag){
        this.userId = userId;
        this.friendId = friendId;
        this.createTime = LocalDateTime.now();
        this.groupTag = groupTag != null ? groupTag : "默认";
        this.status = FriendshipStatus.WAITTING;
    }
}
