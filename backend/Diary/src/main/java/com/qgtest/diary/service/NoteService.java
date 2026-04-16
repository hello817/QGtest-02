package com.qgtest.diary.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.PageResult;
import com.qgtest.diary.common.enums.Visibility;
import com.qgtest.diary.entity.Note;
import com.qgtest.diary.entity.NoteHistory;
import com.qgtest.diary.mapper.AiAnylizeMapper;
import com.qgtest.diary.mapper.FriendshipMapper;
import com.qgtest.diary.mapper.NoteHistoryMapper;
import com.qgtest.diary.mapper.NoteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

//明显user业务有点臃肿了...不能再service大杂烩了（）
@Service
public class NoteService {
    @Autowired
    NoteMapper noteMapper;
    @Autowired
    NoteHistoryMapper noteHistoryMapper;
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
                .orderByDesc(Note::getUpdateTime);
        if (title != null && !title.isEmpty()) {
            wrapper.like(Note::getTitle, title);
        }
        if (tag != null && !tag.isEmpty()) {
            wrapper.like(Note::getTags, tag);  // 简单模糊匹配，进阶可做分词
        }
        IPage<Note> result = noteMapper.selectPage(page, wrapper);
        return PageResult.of(result.getTotal(), pageNum, pageSize, result.getRecords());
    }

    // 查看笔记详情（带权限校验）
    public Note getNoteWithPermission(Long noteId, Long currentUserId) {
        Note note = noteMapper.selectById(noteId);
        if (note == null) throw new BizException("笔记不存在");
        // 权限：仅自己可见且不是本人会被拒绝
        if (note.getVisibility().getCode() == 0 && !note.getUserId().equals(currentUserId)) {
            throw new BizException("无权限查看此笔记");
        }
        // 记录浏览历史
        recordHistory(currentUserId, noteId);
        return note;
    }

    // 设置笔记可见性
    public void setShareLevel(Long noteId, Long userId, Integer shareLevel) {
        Note note = noteMapper.selectById(noteId);
        if (note == null || !note.getUserId().equals(userId)) {
            throw new BizException("无权限修改");
        }
        Visibility visibility = Visibility.formCode(shareLevel);
        if (visibility == null) {
            throw new BizException("无效的可见性级别");
        }
        note.setVisibility(visibility);
        noteMapper.update(note);
    }
    //校验归属权
    public void checkOwnership(Long noteId,Long userId){
        if(noteId == null || userId == null){
            throw new BizException("id部分不能为空");
        }
        Note note = noteMapper.selectById(noteId);
        if(!note.getUserId().equals(userId)){
            throw new BizException("仅作者可更改笔记");//这里后面再搞仅好友可见吧
        };
    }

    // 记录浏览历史
    private void recordHistory(Long userId, Long noteId) {
        NoteHistory history = new NoteHistory();
        history.setUserId(userId);
        history.setNoteId(noteId);
        history.setViewTime(LocalDateTime.now());
        //先删除同一用户对同一笔记的旧记录，保证最近浏览列表不重复
        noteHistoryMapper.delete(new LambdaQueryWrapper<NoteHistory>()
                .eq(NoteHistory::getUserId, userId)
                .eq(NoteHistory::getNoteId, noteId));
        noteHistoryMapper.insert(history);
    }

    // 查询最近浏览的笔记（按时间倒序）
    public List<Note> getRecentHistory(Long userId, int limit) {
        // 查最近浏览记录
        List<NoteHistory> histories = noteHistoryMapper.selectRecentByUserId(userId, limit);
        if (histories.isEmpty()) return Collections.emptyList();

        // 根据 noteId 批量查询笔记（保持顺序）
        List<Long> noteIds = histories.stream()
                .map(NoteHistory::getNoteId)
                .collect(Collectors.toList());
        List<Note> notes = noteMapper.selectBatchIds(noteIds);

        // 按浏览时间排序（因为 selectBatchIds 不保证顺序）
        Map<Long, Note> noteMap = notes.stream().collect(Collectors.toMap(Note::getId, n -> n));
        return noteIds.stream().map(noteMap::get).filter(Objects::nonNull).collect(Collectors.toList());
    }
    // 更新标签（直接替换）,没有做去重合并，前端直接把改后的tag完整传过来改吧
    public void updateTags(Long noteId, String tags){
        Note note = noteMapper.selectById(noteId);
        if (note == null) {
            throw new BizException("笔记不存在");
        }
        note.setTags(tags);
        noteMapper.update(note);
    }
}
