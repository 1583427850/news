package xyz.linyh.essearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.essearch.interceptor.SaveUserIdInterceptor;
import xyz.linyh.essearch.service.ApUserSearchService;
import xyz.linyh.essearch.service.ArticleSearchService;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/article/search")
public class SearchController {

    @Autowired
    private ArticleSearchService articleSearchService;


    @PostMapping("/search")
    public ResponseResult search(@RequestBody UserSearchDto dto){
        //gateway获取用户id todo
        Long userId = SaveUserIdInterceptor.AP_USER_THREAD.get();



        return articleSearchService.search(dto,userId);
    }


}
