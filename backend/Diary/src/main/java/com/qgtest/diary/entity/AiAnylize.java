package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_anylize")
public class AiAnylize {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long noteId;
    private String summary;
    @TableField("tags_sgt")
    private String tags;
    @TableField("key_point")
    private String keyPoint;
    private LocalDateTime createTime;

    public AiAnylize(){}
    public AiAnylize(Long noteId, String summary, String keyPoint, String tags){
        this.noteId = noteId;
        this.summary = summary;
        this.tags = tags;
        this.keyPoint = keyPoint;
        this.createTime = LocalDateTime.now();
    }
}
