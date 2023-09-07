package xyz.linyh.article.controller.v1;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.article.service.ApArticleContentService;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.common.constants.ApArticleConstants;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.article.dto.ArticleHomeDto;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApArticleContent;
import xyz.linyh.model.common.dtos.ResponseResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleHomeController {
    
//    @Autowired
//    private ApArticleService apArticleService;
    @Autowired
    private ApArticleService apArticleService;
    @Autowired
    private ApArticleContentService apArticleContentService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 加载初始化文章
     * @return
     */
    @PostMapping("/load")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
//        return apArticleService.load(dto, ApArticleConstants.LOAD_MORE_TYPE);
        return apArticleService.load2(dto,ApArticleConstants.LOAD_MORE_TYPE,true);
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

    /**
     * 生成文章内容静态为文件
     * @throws IOException
     * @throws TemplateException
     */
    @GetMapping("context_test")
    public void test1() throws IOException, TemplateException {

//        查询数据库获取文章内容数据
            ApArticleContent articleContent = apArticleContentService.getOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, 1302862387124125698L));
            HashMap<String, Object> map = new HashMap<>();
            map.put("content", JSONArray.parseArray(articleContent.getContent()));
            StringWriter writer = new StringWriter();
//        将模板合成后存到writer中
//        将数据和模板合并然后输出到writer中
            configuration.getTemplate("article.ftl").process(map,writer);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", articleContent.getArticleId() + ".html", inputStream);
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,articleContent.getArticleId()).set(ApArticle::getStaticUrl,path));
            System.out.println(path);
        }

}
