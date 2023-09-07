package xyz.linyh.article;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sun.java2d.pipe.SpanIterator;
import xyz.linyh.article.service.ApArticleContentService;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.CacheService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApArticleContent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Set;

@SpringBootTest(classes = ApArticleApplication.class)
public class ArticleTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void test1(){
        Set<String> likes = cacheService.scan(BehaviorConstants.AP_BEHAVIOR_LIKE_PRE + "*");
        for (String like : likes) {
            String[] s = like.split("_");
            Long likeNum = cacheService.hSize(like);
            System.out.println(likeNum);
//            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,s[s.length-1]).set(ApArticle::getLikes,likeNum));
        }
    }


}
