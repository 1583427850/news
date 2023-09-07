package xyz.linyh.article.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.article.service.LoadArticleBehaviorService;
import xyz.linyh.model.article.dto.LoadArticleBehaviorDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1/article")
public class ApLoadBehaviorController {

    @Autowired
    private LoadArticleBehaviorService loadArticleBehaviorService;

    @PostMapping("/load_article_behavior")
    public ResponseResult loadBehavior(@RequestBody LoadArticleBehaviorDto dto){
        return loadArticleBehaviorService.load(dto);
    }
}
