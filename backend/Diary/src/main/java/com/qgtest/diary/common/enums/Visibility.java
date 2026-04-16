package com.qgtest.diary.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.qgtest.diary.common.BizException;
import lombok.Getter;

@Getter
public enum Visibility {
    PRIVATE(0, "仅自己"),
    FRIEND_READ(1, "好友只读"),
    FRIEND_WRITE(2, "好友可编辑"),
    PUBLIC(3, "公开");

    @EnumValue // 告诉MP，存入数据库时取这个字段的值,即enum的value
    private final int code;
    private final String desc;

    Visibility(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static Visibility formCode(Integer code){
      if(code==null){
          return null;
      }
      for(Visibility visibility : values()){
          if(visibility.code == code){
              return visibility;
          }
      }
      throw new BizException("无效的可见性级别" + code);
    };
}
