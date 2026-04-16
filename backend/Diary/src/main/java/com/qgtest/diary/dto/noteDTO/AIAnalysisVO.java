package com.qgtest.diary.dto.noteDTO;

import lombok.Data;

@Data
public class AIAnalysisVO {
    private String summary;
    private String keyPoints;
    private String tags;
    public AIAnalysisVO(String summary , String keyPoints , String tags){
        this.summary = summary;
        this.keyPoints = keyPoints;
        this.tags = tags;
    }
}