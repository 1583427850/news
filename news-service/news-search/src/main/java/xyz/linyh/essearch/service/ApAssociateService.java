package xyz.linyh.essearch.service;

import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface ApAssociateService {

    /**
     * 查询搜索的联想词·
     * @param dto
     * @return
     */
    ResponseResult search(UserSearchDto dto);
}
