package com.qgtest.diary.common;

import lombok.Data;

@Data
public class BizException extends RuntimeException{//继承父类runtimeexception
    private Integer code;
    //自定义消息报错
    public BizException(String msg){
        super(msg);//调用父类中的只用一个参数的构造方法
        this.code = 500;
    }
    //自定义状态码，消息报错
    public BizException(Integer code , String msg){
        super(msg);
        this.code = code;
    }
}
