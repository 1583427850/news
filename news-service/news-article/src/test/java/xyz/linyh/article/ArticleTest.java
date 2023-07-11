package xyz.linyh.article;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.linyh.article.service.ApArticleContentService;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApArticleContent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

@SpringBootTest(classes = ApArticleApplication.class)
public class ArticleTest {


}
