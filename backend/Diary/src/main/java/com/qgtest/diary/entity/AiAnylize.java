package com.qgtest.diary.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("ai_anylize")
public class AiAnylize {
    @TableId(type = IdType.AUTO)
    private long id;
    private long note_id;
    private String summary;
    private String tags_sgt;
    private LocalDateTime createTime;

    public AiAnylize(){}
    public AiAnylize(long note_id , String summary , String tags_sgt){
        this.note_id = note_id;
        this.summary = summary;
        this.tags_sgt = tags_sgt;
        this.createTime = LocalDateTime.now();
    }
}
