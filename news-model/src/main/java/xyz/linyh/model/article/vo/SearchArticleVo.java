package xyz.linyh.model.article.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SearchArticleVo {

//    文章id
    private Long id;
//    文章标题
    private String title;
//    文章发布时间
    private Date publishTime;
//    文章布局
    private Integer layout;
//    文章封面
    private String images;
//    作者id
    private Long authorId;
//    作者名称
    private String authorName;
//    文章静态url
    private String staticUrl;
//    文章内容
    private String content;

}
