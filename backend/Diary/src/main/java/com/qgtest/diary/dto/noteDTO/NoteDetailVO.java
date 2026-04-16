package com.qgtest.diary.dto.noteDTO;
import com.qgtest.diary.entity.AiAnylize;
import com.qgtest.diary.entity.Note;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteDetailVO {
    private Long id;
    private String content;
    private String tags;
    private Integer shareLevel;
    private String summary;
    private String keyPoints;
    private String tagsSuggestion;
    public NoteDetailVO(Note note, AiAnylize ai) {
        this.id = note.getId();
        this.content = note.getContent();
        this.tags = note.getTags();
        this.shareLevel = note.getVisibility().getCode();
        if (ai != null) {
            this.summary = ai.getSummary();
            this.keyPoints = ai.getKeyPoint();
            this.tagsSuggestion = ai.getTags();
        }
    }
}
