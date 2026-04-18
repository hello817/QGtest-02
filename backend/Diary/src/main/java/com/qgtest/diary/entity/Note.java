package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qgtest.diary.common.enums.Visibility;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note")
public class Note {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    public String title;
    private String content;
    private String tags;
    private Visibility visibility;//这里可以考虑做枚举常量
    private Integer isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Note(){};
    public Note(Long userId, String content,String title, String tags){
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.tags = tags;//要考虑一下怎么更新、添加tags
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
