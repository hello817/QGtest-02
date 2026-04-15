package com.qgtest.diary.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum FriendshipStatus {
    WAITING(0,"待同意"),
    ACCEPTED(1,"已同意"),
    REJECTED(2,"已拒绝"),
    DELETED(3,"已拒绝");

    @EnumValue
    private final int code;
    private final String desc;

    FriendshipStatus(int code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
