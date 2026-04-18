package com.qgtest.diary.mapper;

import com.qgtest.diary.entity.Friendship;
import com.qgtest.diary.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendshipMapper {
    //这里要做申请好友，同意/拒绝申请，删除好友,如果加的是之前拒绝过的直接在业务层判断然后执行更新方法
    @Insert("insert into friendship(user_id,friend_id,state,group_tag) values (#{userId},#{friendId},#{status.coode()},#{groupTag})")
    int insert(Friendship friendship);

    @Select("select * from friendship where id = #{id}")
    Friendship selectById(Long id);
     //用JOIN来获取全部好友
    @Select("select u.* from user u INNER JOIN friendship f ON u.id = f.friend_id where f.user_id = #{userId} and f.state = 1")
    List<User> selectAllFriends(@Param("userId") Long userId);
    //按分组搜索好友
    @Select("SELECT u.*, f.group_tag FROM user u INNER JOIN friendship f ON u.id = f.friend_id WHERE f.user_id = #{userId} AND f.state = 1 AND f.group_tag = #{groupTag}")
    List<User> selectFriendsByGroup(@Param("userId") Long userId, @Param("groupTag") String groupTag);
    //搜索好友分组
    @Select("SELECT DISTINCT group_tag FROM friendship WHERE user_id = #{userId} AND state = 1")
    List<String> selectUserGroups(@Param("userId") Long userId);

    //用朋友id搜索
    @Select("select * from friendship where (user_id = #{userId} and friend_id = #{friendId})")
    Friendship selectByFriendId(@Param("userId") Long userId,@Param("friendId") Long friendId);
    //查询所有好友请求(只有待同意可以操作)
    @Select("select * from friendship where (user_id = #{userId} or friend_id = #{userId}) and state = #{state}")
    List<Friendship> selectAllRequests(@Param("userId") Long userId, @Param("state") Integer state);

    //查找关系，用于判断好友状态
    @Select("select * from friendship where (user_id = #{userId} and friend_id = #{friendId}) or (user_id = #{friendId} and friend_id = #{userId})")
    Friendship isFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);

    //同意申请/拒绝申请、删除好友
    @Update("UPDATE friendship SET state = #{state} WHERE user_id = #{userId} AND friend_id = #{friendId}")
    int update(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("state") Integer state);
    //更新好友分组
    @Update("UPDATE friendship SET group_tag = #{groupTag} WHERE user_id = #{userId} AND friend_id = #{friendId} AND state = 1")
    int updateFriendGroup(@Param("userId") Long userId, @Param("friendId") Long friendId, @Param("groupTag") String groupTag);

}
