package xyz.linyh.model.article.dto;

import lombok.Data;
import xyz.linyh.model.article.entity.ApArticle;

@Data
public class ApArticleHot extends ApArticle {
    private Integer score;
}
