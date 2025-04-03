package com.guestroom.Vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class PageVo implements Serializable {
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页数量
     */
    private Integer pageSize = 10;
    
    /**
     * 关键字
     */
    private String keyword;

    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
}
