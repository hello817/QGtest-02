package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note")
public class Note {
    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private String content;
    private String tags;
    private Integer visibility;
    private Integer isDelete;
    private LocalDateTime createTime;
    private LocalDateTime updaTetime;

    public Note(){};
    public Note(long userId,String content,String tags,Integer visibility){
        this.userId = userId;
        this.content = content;
        this.tags = tags;//要考虑一下怎么更新、添加tag啊
        this.visibility = visibility;
        this.createTime = LocalDateTime.now();
        this.updaTetime = LocalDateTime.now();
    }
}
