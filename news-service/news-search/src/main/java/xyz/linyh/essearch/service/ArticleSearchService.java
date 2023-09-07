package xyz.linyh.essearch.service;

import org.bouncycastle.asn1.ocsp.ResponderID;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;

//用来查询es下article文章
public interface ArticleSearchService {

    /**
     * 查询es下的文章
     * @param dto
     * @return
     */
    ResponseResult search(UserSearchDto dto,Long userId);
}
