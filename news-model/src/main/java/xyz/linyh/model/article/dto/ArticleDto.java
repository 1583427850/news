package xyz.linyh.model.article.dto;

import lombok.Data;
import xyz.linyh.model.article.entity.ApArticle;

@Data
public class ArticleDto extends ApArticle {
    //文章内容
    private String content;

}
