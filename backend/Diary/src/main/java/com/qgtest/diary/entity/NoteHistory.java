package com.qgtest.diary.entity;

import com.qgtest.diary.mapper.NoteMapper;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteHistory {
    private Long userId;
    private Long noteId;
    private LocalDateTime viewTime;
    public NoteHistory(){};
    public NoteHistory(Long userId , Long noteId){
        this.userId = userId;
        this.noteId = noteId;
    }
}
