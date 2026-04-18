package com.qgtest.diary.dto.noteDTO;
import com.qgtest.diary.entity.AiAnylize;
import com.qgtest.diary.entity.Note;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDetailVO {
    private Long id;
    private String title;
    private String content;
    private String tags;
    private Integer visibility;
    private LocalDateTime updateTime;
    private String summary;
    private String keyPoints;
    private String tagsSuggestion;
    
    public NoteDetailVO(Note note, AiAnylize ai) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.tags = note.getTags();
        this.visibility = note.getVisibility().getCode();
        this.updateTime = note.getUpdateTime();
        if (ai != null) {
            this.summary = ai.getSummary();
            this.keyPoints = ai.getKeyPoint();
            this.tagsSuggestion = ai.getTags();
        }
    }
}
