package com.qgtest.diary.controller;

import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.PageResult;
import com.qgtest.diary.common.Result;
import com.qgtest.diary.dto.noteDTO.*;
import com.qgtest.diary.entity.Friendship;
import com.qgtest.diary.entity.Note;
import com.qgtest.diary.entity.AiAnylize;
import com.qgtest.diary.mapper.AiAnylizeMapper;
import com.qgtest.diary.mapper.FriendshipMapper;
import com.qgtest.diary.mapper.NoteMapper;
import com.qgtest.diary.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired private NoteService noteService;
    @Autowired private AIService aiService;
    @Autowired private NoteMapper noteMapper;
    @Autowired private FriendshipMapper friendshipMapper;
    @Autowired
    private AiAnylizeMapper aiAnylizeMapper;

    @PostMapping
    public Result<Void> createNote(@RequestBody @Valid NoteCreateDTO dto,
                                   @RequestAttribute Long userId) {
        noteService.addNote(userId,dto.getTitle(), dto.getContent(), dto.getTags());
        return Result.success();
    }

    @GetMapping
    public Result<PageResult<Note>> listNotes(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestAttribute Long userId) {
        PageResult<Note> page = noteService.listMyNotes(userId, pageNum, pageSize, title, tag);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<NoteDetailVO> getNote(@PathVariable Long id,
                                        @RequestAttribute Long userId) {
        Note note = noteService.getNoteWithPermission(id, userId);
        // 查询AI分析结果
        AiAnylize ai = aiService.selectByNoteId(id);
        NoteDetailVO vo = new NoteDetailVO(note, ai);
        return Result.success(vo);
    }

    @PutMapping("/{id}")
    public Result<Void> updateNote(@PathVariable Long id,
                                   @RequestBody @Valid NoteUpdateDTO dto,
                                   @RequestAttribute Long userId) {
        // 校验笔记归属
        noteService.checkOwnership(id, userId);
        if (dto.getTitle() != null) {
            noteService.updateTitle(id, dto.getTitle());
        }
        if (dto.getContent() != null) {
            noteService.updateNoteContent(id, dto.getContent());
        }
        if (dto.getTags() != null) {
            noteService.updateTags(id, dto.getTags());
        }
        return Result.success();
    }

    @PutMapping("/{id}/visibility")
    public Result<Void> setVisibility(@PathVariable Long id,
                                      @RequestParam Integer shareLevel,
                                      @RequestAttribute Long userId) {
        noteService.setShareLevel(id, userId, shareLevel);
        return Result.success();
    }

    @PutMapping("/{id}/trash")
    public Result<Void> trashNote(@PathVariable Long id, @RequestAttribute Long userId) {
        noteService.checkOwnership(id, userId);
        noteService.trashNote(id);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNote(@PathVariable Long id, @RequestAttribute Long userId) {
        noteService.checkOwnership(id, userId);
        noteService.deleteNote(id);
        return Result.success();
    }

    @GetMapping("/history")
    public Result<List<Note>> getHistory(@RequestAttribute Long userId,
                                         @RequestParam(defaultValue = "10") int limit) {
        return Result.success(noteService.getRecentHistory(userId, limit));
    }

    @PostMapping("/{id}/analyze")
    public Result<AIAnalysisVO> analyzeNote(@PathVariable Long id,
                                            @RequestAttribute Long userId) {
        Note note = noteService.getNoteWithPermission(id, userId);
        String summary = aiService.generateSummary(note.getContent());
        String keyPoints = aiService.generateKeyPoint(note.getContent());
        String tags = aiService.generateTags(note.getContent());
        aiService.saveAnalysis(id, summary, keyPoints, tags);
        return Result.success(new AIAnalysisVO(summary, keyPoints, tags));
    }
    @GetMapping("/share/{noteId}")
    public Result<NoteDetailVO> shareNote(@PathVariable Long noteId, @RequestAttribute(required = false) Long userId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null) {
            throw new BizException("笔记不存在");
        }

        Integer shareLevel = note.getVisibility().getCode();
        if (shareLevel == 0) {
            if (userId == null || !userId.equals(note.getUserId())) {
                throw new BizException("无权限访问");
            }
        } else if (shareLevel == 3) {
            // 所有人可见，允许访问
        } else if (shareLevel == 1 || shareLevel == 2) {
            if (userId == null) {
                throw new BizException("请先登录");
            }
            if (!note.getUserId().equals(userId)) {
                Friendship friendship = friendshipMapper.isFriend(userId, note.getUserId());
                if (friendship == null || friendship.getStatus().getCode() != 1) {
                    throw new BizException("仅好友可见");
                }
            }
        }

        AiAnylize aiAnylize = aiAnylizeMapper.selectByNoteId(noteId);
        NoteDetailVO vo = new NoteDetailVO(note, aiAnylize);
        return Result.success(vo);
    }
}
