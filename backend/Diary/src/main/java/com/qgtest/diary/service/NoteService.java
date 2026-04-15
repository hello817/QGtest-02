package com.qgtest.diary.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.PageResult;
import com.qgtest.diary.entity.Note;
import com.qgtest.diary.mapper.AiAnylizeMapper;
import com.qgtest.diary.mapper.FriendshipMapper;
import com.qgtest.diary.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//明显user业务有点臃肿了...不能再service大杂烩了（）
@Service
public class NoteService {
    @Autowired
    NoteMapper noteMapper;
    @Autowired
    FriendshipMapper friendshipMapper;
    @Autowired
    AiAnylizeMapper aiAnylizeMapper;
    @Autowired
    AIService aiService;
//========================userService迁移数据部分
    @Transactional
    public List<Note> findMyNote(Long userId){
        return noteMapper.selectByUserId(userId);
    }
    @Transactional
    public List<Note> findAllVisibleNote(){
        return noteMapper.selectAllVisibleNote();
    }
    @Transactional
    public void addNote(Long userId, String content, String tags){
        Note note = new Note(userId,content,tags);
        noteMapper.insert(note);
    }
    //回收站
    @Transactional
    public void trashNote(Long noteId){
        noteMapper.trashById(noteId,1);
    }
    //取出
    @Transactional
    public void cancelTrashNote(Long noteId){
        noteMapper.trashById(noteId,0);
    }
    @Transactional
    public void deleteNote(Long noteId) {
        //删除笔记
        noteMapper.deleteById(noteId);

        //删除关联的AI分析
        aiAnylizeMapper.deleteByNoteId(noteId);
    }
    //更新
    public void updateNoteContent(Long noteId, String content){
        Note note = noteMapper.selectById(noteId);
        note.setContent(content);
        noteMapper.update(note);
    }
//=========================================================================
// 分页查询我的笔记（支持标题模糊、标签筛选）
    public PageResult<Note> listMyNotes(Long userId, int pageNum, int pageSize, String title, String tag) {
        Page<Note> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Note> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Note::getUserId, userId)
                .eq(Note::getIsDelete, 0)  // 未被逻辑删除
                .orderByDesc(Note::getUpdaTetime);
        if (title != null && !title.isEmpty()) {
            wrapper.like(Note::getTitle, title);
        }
        if (tag != null && !tag.isEmpty()) {
            wrapper.like(Note::getTags, tag);  // 简单模糊匹配，进阶可做分词
        }
        page<Note> result = noteMapper.selectPage(page, wrapper);
        return PageResult.of(result.getTotal(), pageNum, pageSize, result.getRecords());
    }

    // 查看笔记详情（带权限校验）
    public Note getNoteWithPermission(Long noteId, Long currentUserId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null) throw new BizException("笔记不存在");
        // 权限：仅自己可见且不是本人 -> 拒绝
        if (note.getVisibility() == 0 && !note.getUserId().equals(currentUserId)) {
            throw new BizException("无权限查看此笔记");
        }
        // 记录浏览历史（异步或同步）
        recordHistory(currentUserId, noteId);
        return note;
    }

    // 设置笔记可见性
    public void setShareLevel(Long noteId, Long userId, Integer shareLevel) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new BizException("无权限修改");
        }
        note.setVisibility(shareLevel);
        noteMapper.update(note);
    }

    // 记录浏览历史（需要新建 history 表）
    private void recordHistory(Long userId, Long noteId) {
        // 实现插入或更新 note_history 表，记录最近浏览时间
    }

    // 查询最近浏览的笔记（按时间倒序）
    public List<Note> getRecentHistory(Long userId, int limit) {
        // 关联 note_history 表查询
    }
}
