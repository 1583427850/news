package xyz.linyh.essearch.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.essearch.EsSearchApplication;
import xyz.linyh.essearch.service.ArticleSearchService;
import xyz.linyh.model.article.dto.UserSearchDto;

import java.util.Date;

@SpringBootTest(classes = EsSearchApplication.class)
class ArticleSearchServiceImplTest {

    @Autowired
    private ArticleSearchService articleSearchService;

    @Test
    void search() {
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setPageSize(10);
        userSearchDto.setPageNum(10);
        userSearchDto.setSearchWords("测试");
        userSearchDto.setMinBehotTime(new Date());
//        System.out.println(articleSearchService.search(userSearchDto).getData());
    }
}