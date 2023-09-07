package xyz.linyh.article.service;

import xyz.linyh.model.article.dto.LoadArticleBehaviorDto;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface LoadArticleBehaviorService {
    /**
     * 加载文章的行为
     * @param dto
     * @return
     */
    ResponseResult load(LoadArticleBehaviorDto dto);
}
