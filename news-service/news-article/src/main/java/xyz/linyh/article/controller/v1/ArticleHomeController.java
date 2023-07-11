package xyz.linyh.article.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.common.constants.ApArticleConstants;
import xyz.linyh.model.article.dto.ArticleHomeDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {
    
    @Autowired
    private ApArticleService apArticleService;

    /**
     * 加载初始化文章
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto, ApArticleConstants.LOAD_MORE_TYPE);
    }

    /**
     * 加载更多以前的文章
     * @return
     */
    @PostMapping("/loadmore")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto,ApArticleConstants.LOAD_MORE_TYPE);
    }

    /**
     * 加载最新文章
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto,ApArticleConstants.LOAD_NEWS_TYPE);
    }
}
