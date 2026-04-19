package com.qgtest.diary.dto.noteDTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data

public class SharedNoteDTO {
    private Long id;
    private String title;
    private String content;
    private String tags;
    private String visibility;
    private Long authorId;
    private String authorName;
    private String authorAccount;
    private String authorAvatar;
    private LocalDateTime updateTime;
}
