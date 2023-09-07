package xyz.linyh.feign.FeignFallBack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.linyh.feign.IArticleClient;
import xyz.linyh.model.article.dto.ArticleDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@Component
@Slf4j
public class IArticleClientFallBack implements IArticleClient
{
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        log.error("调用到了IArticleClientFallBack.saveArticle的fallback");
        return null;
    }
}
