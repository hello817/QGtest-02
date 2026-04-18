package com.qgtest.diary.controller;

import com.qgtest.diary.common.BizException;
import com.qgtest.diary.common.PageResult;
import com.qgtest.diary.common.Result;
import com.qgtest.diary.dto.friendDTO.*;
import com.qgtest.diary.entity.User;
import com.qgtest.diary.entity.Friendship;
import com.qgtest.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {
    @Autowired private UserService userService;
    //查找用户
    @GetMapping("/search")
    public Result<List<User>> search(@RequestParam String keyword) {
        return Result.success(userService.findUserByKeyWord(keyword));
    }
    //发送请求,分组可有可无，默认为“默认”分组
    @PostMapping("/requests/{friendId}")
    public Result<Void> sendRequest(@PathVariable Long friendId,
                                    @RequestParam(required = false, defaultValue = "默认") String groupTag,
                                    @RequestAttribute Long userId) {
        userService.sendFriendRequest(userId, friendId, groupTag);
        return Result.success();
    }
    //查询请求
    @GetMapping("/requests")
    public Result<List<Friendship>> getRequests(@RequestAttribute Long userId,
                                                @RequestParam(required = false) Integer state) {
        if (state == null || state == 0) {
            return Result.success(userService.findAllWaittingRequest(userId));
        } else if (state == 1) {
            return Result.success(userService.findAllAcceptedRequest(userId));
        } else {
            return Result.success(userService.findAllRejectedRequest(userId));
        }
    }
    //接受请求
    @PutMapping("/requests/{requestId}/accept")
    public Result<Void> acceptRequest(@PathVariable Long requestId,
                                      @RequestAttribute Long userId) {
        Friendship fs = userService.getFriendshipById(requestId);
        if (!fs.getFriendId().equals(userId)) {
            throw new BizException("无权操作");
        }
        userService.resetFriendRequestState(fs.getUserId(), fs.getFriendId(), 1);
        return Result.success();
    }
    //拒绝请求
    @PutMapping("/requests/{requestId}/reject")
    public Result<Void> rejectRequest(@PathVariable Long requestId,
                                      @RequestAttribute Long userId) {
        Friendship fs = userService.getFriendshipById(requestId);
        if (!fs.getFriendId().equals(userId)) {
            throw new BizException("无权操作");
        }
        userService.resetFriendRequestState(fs.getUserId(), fs.getFriendId(), 2);
        return Result.success();
    }
    //好友列表/按分组查询好友列表
    @GetMapping
    public Result<List<User>> listFriends(@RequestAttribute Long userId,
                                          @RequestParam(required = false) String group) {
        if (group != null && !group.isEmpty()) {
            return Result.success(userService.getFriendsByGroup(userId, group));
        }
        return Result.success(userService.findAllFriends(userId));
    }
    //查询好友分组
    @GetMapping("/groups")
    public Result<List<String>> getUserGroups(@RequestAttribute Long userId) {
        return Result.success(userService.getUserGroups(userId));
    }
    //更新好友分组
    @PutMapping("/{friendId}/group")
    public Result<Void> updateFriendGroup(@PathVariable Long friendId,
                                          @RequestBody @Valid FriendGroupDTO dto,
                                          @RequestAttribute Long userId) {
        userService.updateFriendGroup(userId, friendId, dto.getGroupTag());
        return Result.success();
    }
    //删除好友
    @DeleteMapping("/{friendId}")
    public Result<Void> deleteFriend(@PathVariable Long friendId,
                                     @RequestAttribute Long userId) {
        userService.resetFriendRequestState(userId, friendId, 3);
        return Result.success();
    }
}
