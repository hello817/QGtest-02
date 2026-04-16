package com.qgtest.diary.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qgtest.diary.entity.NoteHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NoteHistoryMapper extends BaseMapper<NoteHistory> {
    // 查询最近N条浏览记录（按时间倒序）
    @Select("SELECT * FROM note_history WHERE user_id = #{userId} ORDER BY view_time DESC LIMIT #{limit}")
    List<NoteHistory> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") int limit);
}
