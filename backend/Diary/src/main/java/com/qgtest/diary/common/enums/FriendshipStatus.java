package com.qgtest.diary.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.qgtest.diary.common.BizException;
import lombok.Getter;

@Getter
public enum FriendshipStatus {
    WAITTING(0,"待同意"),
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
    public static FriendshipStatus formCode(Integer code){
        if(code==null){
            return null;
        }
        for(FriendshipStatus status : values()){
            if(status.code == code){
                return status;
            }
        }
        throw new BizException("无效的可见性级别" + code);
    };
}
