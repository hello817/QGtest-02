package com.qgtest.diary.mapper;

import com.qgtest.diary.entity.Friendship;
import com.qgtest.diary.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendshipMapper {
    //这里要做申请好友，同意/拒绝申请，删除好友,如果加的是之前拒绝过的直接在业务层判断然后执行更新方法
    @Insert("insert into friendship(user_id,friend_id) values ()")
    int insert(Friendship friendship);

    //用JOIN来获取全部好友
    @Select("select u.* from user u INNER JOIN friendship f ON u.id = f.friend_id where f.userId = #{userId} and f.status = 1")
    List<User> selectAllFriends(long userId);

    //查找关系，用于判断好友状态
    @Select("select * from friendship where user_id = #{userId} and friend_id = #{friendId}")
    Friendship selectById(@Param("userId") long userId ,@Param("friendId") long friendId);

    //同意申请/拒绝申请、删除好友
    @Update("UPDATE friendship SET status = #{status} WHERE user_id = #{userId} AND friend_id = #{friend_id}")
    int update(Friendship friendship);

}
