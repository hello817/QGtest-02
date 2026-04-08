package com.qgtest.diary.common;

import lombok.Data;
/*
 *   可以用@NoArgsConstructor
 *   和@AllArgsContructor：
 *   分别生成无参和有参的构造方法，这里为了熟悉语言直接构造了
 */
@Data
public class Result<T> {
    private Integer code;//状态码
    private String msg;//消息
    private T data;//返回数据

    public Result() {}
    public Result(Integer code,String msg,T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //成功，带数据响应，应为类级别，不需要创建对象就能用
    public static <T> Result<T> success(T data){
        return new Result<>(200,"success",data);
    }
    //成功，不带数据响应
    public static <T> Result<T> success(){
        return new Result<>(200,"success",null);
    }
    //失败，自定义消息
    public static <T> Result<T> erro(String msg){
        return new Result<>(500,msg,null);
    }
    //失败，自定义状态码和消息
    public static <T> Result<T> erro(Integer code,String msg){
        return new Result<>(code,msg,null);
    }
}
