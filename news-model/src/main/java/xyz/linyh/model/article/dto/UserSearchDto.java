package xyz.linyh.model.article.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserSearchDto {

//    搜索的关键词
    private String searchWords;

//    当前页面
    private int pageNum;

//    当前页面大小
    private int pageSize;

//    最小时间
    private Date minBehotTime;
}
