package com.qgtest.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgtest.diary.entity.AiAnylize;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AiAnylizeMapper extends BaseMapper<AiAnylize> {

    //插入AI分析结果
    @Insert("INSERT INTO ai_anylize(note_id, summary, key_point, tags_sgt, create_time) " +
            "VALUES (#{noteId}, #{summary}, #{keyPoint}, #{tags}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AiAnylize aiAnylize);

    //根据笔记ID查询AI分析结果
    @Select("SELECT id, note_id, summary, key_point, tags_sgt AS tags, create_time, update_time FROM ai_anylize WHERE note_id = #{noteId}")
    AiAnylize selectByNoteId(@Param("noteId") Long noteId);

    //更新AI分析结果（如果存在则更新，不存在则插入）
    @Update("UPDATE ai_anylize SET summary = #{summary}, key_point = #{keyPoint}, " +
            "tags_sgt = #{tags}, update_time = NOW() WHERE note_id = #{noteId}")
    int updateByNoteId(AiAnylize aiAnylize);

    //删除指定笔记的AI分析
    @Delete("DELETE FROM ai_anylize WHERE note_id = #{noteId}")
    int deleteByNoteId(@Param("noteId") Long noteId);


    //批量删除多个笔记的AI分析（用于批量删除笔记时）
    @Delete("<script>" +
            "DELETE FROM ai_anylize WHERE note_id IN " +
            "<foreach collection='noteIds' item='noteId' open='(' separator=',' close=')'>" +
            "#{noteId}" +
            "</foreach>" +
            "</script>")
    int deleteBatchByNoteIds(@Param("noteIds") List<Long> noteIds);

    //查询用户的AI分析统计信息
    @Select("SELECT COUNT(*) FROM ai_anylize a " +
            "INNER JOIN note n ON a.note_id = n.id " +
            "WHERE n.user_id = #{userId} AND n.is_delete = 0")
    int countByUserId(@Param("userId") Long userId);
}

