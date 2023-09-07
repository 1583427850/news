package xyz.linyh.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.article.dto.ApCollectionDto;
import xyz.linyh.model.article.dto.ArticleDto;
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

    /**
     * 加载文章，优先加载热度较高的文章
     * @param dto
     * @param type
     * @param isHome
     * @return
     */
    public ResponseResult load2(ArticleHomeDto dto,int type,Boolean isHome);

    /**
     * 保存或修改app端的文章
     * @param dto
     * @return
     */
    ResponseResult saveOrUpdateArticle(ArticleDto dto);


}
