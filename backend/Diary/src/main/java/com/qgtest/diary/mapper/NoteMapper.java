package com.qgtest.diary.mapper;

import com.qgtest.diary.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    //这里先做一下基础的+更改可见性，逻辑删除（回收站），添加标签，查找好友笔记,有缺的想到再做吧
    @Insert("insert into note(user_id,content,tags) values (#{userId},#{content},#{tags})")//突然发现笔记好像没做标题...?
    int insert(Note note );
    @Update("UPDATE note SET content = #{content},tags = #{tags},visibility = #{visibility},is_delete = #{isDelete} update_time = NOW() WHERE id = #{id}")
    int update(Note note);
    @Select("select * from note where id = #{id}")
    Note selectById(@Param("id") Long id);
    //tag做模糊搜索
    @Select("select * from note where user_id = #{userId} and tags like %#{tags}%")
    Note selectByTags(@Param("userId") Long userId, @Param("tags") String tags);
    @Select("select * from note where visibility = 1")
    List<Note> selectAllVisibleNote();//查找所有可见（所有用户共享的）笔记，最好是只读或者类似fork笔记到自己的知识库，或者这部分也可以不做？
    @Select("select * from note where user_id = #{userId} order by create_time desc ")//或许应该换个排序方式
    List<Note> selectByUserId(@Param("userId") Long userId);
    //删除:回收站+永久删除
    @Update("UPDATE note SET is_delete = #{code}")
    int trashById(@Param("id") Long id, @Param("code") Integer code);//这里用code可以直接一个接口两个用法，丢进回收站/从回收站放出
    @Delete("DELETE FROM note WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}

