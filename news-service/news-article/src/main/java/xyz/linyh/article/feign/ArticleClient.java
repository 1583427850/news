package xyz.linyh.article.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.model.article.dto.ArticleDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
public class ArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 保存或修改app端文章方法
     * @param dto
     * @return 里面包含保存后的文章id
     */
    @PostMapping("/api/v1/article/save")
    public ResponseResult saveArticle(@RequestBody ArticleDto dto){
        return apArticleService.saveOrUpdateArticle(dto);
    }
}
