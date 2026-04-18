package com.qgtest.diary.dto.noteDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteCreateDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String tags;  // 逗号分隔
}
