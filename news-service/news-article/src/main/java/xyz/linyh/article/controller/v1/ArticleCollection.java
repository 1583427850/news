package xyz.linyh.article.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.ApCollectionService;
import xyz.linyh.model.article.dto.ApCollectionDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1")
public class ArticleCollection {

    @Autowired
    private ApCollectionService apCollectionService;



    @PostMapping("/collection_behavior")
    public ResponseResult collection(@RequestBody ApCollectionDto dto){
        return apCollectionService.collection(dto);
    }
}
