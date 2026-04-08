package com.qgtest.diary.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import java.util.List;

@Data
public class PageResult<T> {
    private long total;//数据总量
    private Integer pageNum;//页码
    private Integer pageSize;//每页信息数量
    private List<T> data;

    public PageResult(){}
    public PageResult(long total,Integer pageNum,Integer pageSize,List<T> data){
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.data = data;
    }

    public static <T> PageResult<T> of(long total,Integer pageNum,Integer pageSize,List<T> data){
        return new PageResult<>(total,pageNum,pageSize,data);
    }
}
