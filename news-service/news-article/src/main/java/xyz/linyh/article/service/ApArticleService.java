package xyz.linyh.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.article.dto.ArticleHomeDto;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 查询文章概述
     * @param dto
     * @param type 1为查询更多 2为加载最新
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto,int type);
}
