package xyz.linyh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.linyh.feign.FeignFallBack.IArticleClientFallBack;
import xyz.linyh.model.article.dto.ArticleDto;
import xyz.linyh.model.common.dtos.ResponseResult;

/**
 * 定义远程调用的接口和调用哪个微服务
 */
@FeignClient(value = "news-article",fallback = IArticleClientFallBack.class)
public interface IArticleClient {

    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto);
}
