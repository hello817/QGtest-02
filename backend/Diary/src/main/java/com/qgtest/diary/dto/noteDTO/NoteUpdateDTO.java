package com.qgtest.diary.dto.noteDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NoteUpdateDTO {
    private String content;
    private String tags;
}
