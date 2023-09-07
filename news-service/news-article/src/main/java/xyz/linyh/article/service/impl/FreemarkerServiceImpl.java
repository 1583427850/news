package xyz.linyh.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.article.service.ApArticleConfigService;
import xyz.linyh.article.service.ApArticleContentService;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.FreemarkerService;
import xyz.linyh.common.exception.CustomException;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApArticleContent;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;


@Service
@Slf4j
@Transactional
public class FreemarkerServiceImpl implements FreemarkerService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 将文章内容用freemarker转换成一个文件然后保存到minio中
     */

    @Async
    @Override
    public void saveArticleContentToMinIO(Long apArticleId) {
        if(apArticleId==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }
        try {
            Template template = configuration.getTemplate("article.ftl");

//        根据文章id获取文章内容
            ApArticleContent articleContent = apArticleContentService.getOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, apArticleId));
            StringWriter writer = new StringWriter();
//        将模板合成后存到writer中
//        将数据和模板合并然后输出到writer中
            HashMap<String, Object> map = new HashMap<>();
            map.put("content",JSON.parseArray(articleContent.getContent()));
            template.process(map,writer);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", articleContent.getArticleId() + ".html", inputStream);
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,articleContent.getArticleId()).set(ApArticle::getStaticUrl,path));
        } catch (Exception e) {
            log.error("文章内容保存失败");
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }


    }
}
