package xyz.linyh.article.service.impl;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.article.ApArticleApplication;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.model.article.dto.ArticleDto;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApArticleApplication.class)
class ApArticleServiceImplTest {

    @Autowired
    private ApArticleService apArticleService;

    @Test
    void saveOrUpdateArticle() {

        ArticleDto articleDto = new ArticleDto();
        articleDto.setContent("123");
        articleDto.setTitle("test");
        articleDto.setAuthorId(1234);
        articleDto.setAuthorName("hello");
        articleDto.setChannelId(1);
        articleDto.setChannelName("test");
        articleDto.setLayout(0);
        articleDto.setFlag(0);
        articleDto.setImages("");
        articleDto.setLabels("");


        articleDto.setSyncStatus(0);
        articleDto.setOrigin(0);
        articleDto.setStaticUrl("");


        apArticleService.saveOrUpdateArticle(articleDto);
    }
}