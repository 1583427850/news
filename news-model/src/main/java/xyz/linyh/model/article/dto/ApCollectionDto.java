package xyz.linyh.model.article.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApCollectionDto {

    //文章id
    private Long entryId;

    //类型 0为文章
    private Integer type;

    //操作0为收藏 1为取消收藏
    private Integer operation;

    private Date publishedTime;
}
