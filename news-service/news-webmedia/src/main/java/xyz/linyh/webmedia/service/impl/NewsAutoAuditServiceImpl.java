package xyz.linyh.webmedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import xyz.linyh.common.ORC.Tess4jClient;
import xyz.linyh.feign.IArticleClient;
import xyz.linyh.common.autoaudit.ImageAudit;
import xyz.linyh.common.autoaudit.TextAudit;
import xyz.linyh.common.constants.WmMediaConstans;
import xyz.linyh.common.exception.CustomException;
import xyz.linyh.file.service.FileStorageService;
import xyz.linyh.model.article.dto.ArticleDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.webmedia.entity.WmChannel;
import xyz.linyh.model.webmedia.entity.WmNews;
import xyz.linyh.model.webmedia.entity.WmSensitive;
import xyz.linyh.model.webmedia.entity.WmUser;
import xyz.linyh.utils.common.SensitiveWordUtil;
import xyz.linyh.webmedia.service.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * 对文章的内容进行审核
 */
@Service
@Slf4j
public class NewsAutoAuditServiceImpl implements NewsAutoAuditService {

    @Autowired
    private WmNewsService wmNewsService;

    @Autowired
    private ImageAudit imageAudit;

    @Autowired
    private TextAudit textAudit;

    @Autowired
    private IArticleClient iArticleClient;

    @Autowired
    private WmUserService wmUserService;
    /**
     * 对文章的内容进行审核
     *
     * @param newsId
     */
//    开启异步调用，可以不用等这个方法执行完成才执行接下来的代码
    @Async
    @Override
    public boolean auditText(Long newsId) {
//        1. 参数校验
        if (newsId==null){
            log.error("文章id不能为空");
            return false;
        }
//        2. 获取文章的内容和图片
        WmNews wmNews = wmNewsService.getOne(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getId, newsId));
        if(wmNews==null){
            log.error("查找不到对应文章");
            return false;
        }

//        3. 抽取图片和文字
        Map<String,Object> textAndImage = getTextAndImage(wmNews);

//        新功能：之文字敏感词过滤 todo
        Boolean isTextSensitiveAudit = auditTextSensitive((String) textAndImage.get("texts"),wmNews);
        if(!isTextSensitiveAudit)return false;

//        新功能：之图片敏感词过滤 todo
        Boolean isImageSensitiveAudit = auditImageSensitive(textAndImage,wmNews);
        if(!isImageSensitiveAudit)return false;


//        4. 审核是否有违规内容 这里只要里面包含违规2字就是违规内容 待更新 todo
        boolean isAudit = auditTextAndImage(textAndImage,wmNews);
        if(!isAudit)return false;
//        5. 如果审核通过，那么调用news-article里面的方法创建对应的文章 异步调用
        ResponseResult responseResult = saveToArticle(wmNews);

//        然后将news的article文章字段赋值,和将状态码改为已经发布
        if(responseResult!=null && responseResult.getData()!=null){
            wmNews.setArticleId((Long) responseResult.getData());
            LambdaUpdateWrapper<WmNews> wrapper = new LambdaUpdateWrapper<WmNews>();
            wrapper.eq(WmNews::getId,wmNews.getId())
                            .set(WmNews::getArticleId,(Long)responseResult.getData())
                                    .set(WmNews::getStatus,WmMediaConstans.NEWS_STATUS_OK);
            wmNewsService.update(wrapper);
            return true;
        }
        return false;
    }

    @Autowired
    private Tess4jClient tess4jClient;
    @Autowired
    private FileStorageService fileStorageService;

    private boolean auditImageSensitive(Map<String, Object> textAndImage,WmNews wmNews) {
//        1. 参数校验
        if(textAndImage==null){
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        try {
//        2. 获取图片的url
            List<String> images = (List<String>) textAndImage.get("images");
//        3. 根据url获取图片
            for (String image : images) {
                byte[] bytes = fileStorageService.downLoadFile(image);
                ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                BufferedImage imageFile = ImageIO.read(in);
    //        4. tess4jClient获取所有图片上的文字
                String result = tess4jClient.doOCR(imageFile);
                Boolean aBoolean = auditTextSensitive(result, wmNews);
                if(!aBoolean)return false;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;



    }


    @Autowired
    private WmSensitiveService wmSensitiveService;
    /**
     * 审核文章中，是否有敏感词
     * @param text
     * @return
     */
    private Boolean auditTextSensitive(String text,WmNews wmNews) {

//        1. 先获取所有敏感词
        List<WmSensitive> wmSensitiveList = wmSensitiveService.list();
//        2. 将敏感词拿出来存到一个list中
        List<String> wmSensitiveContent = new ArrayList<>();
        for (WmSensitive wmSensitive : wmSensitiveList) {
            wmSensitiveContent.add(wmSensitive.getSensitives());
        }
//        3. 初始化敏感词字典
        SensitiveWordUtil.initMap(wmSensitiveContent);
//        4. 判断是否有敏感词
        Map<String, Integer> auditResult = SensitiveWordUtil.matchWords(text);
//        5. 如果有敏感词，那么直接修改文章表 返回false
        if(auditResult.size()>0){
            wmNews.setReason(auditResult.keySet().toString());
            wmNews.setStatus(WmMediaConstans.NEWS_STATUS_FAIL);
            wmNewsService.updateById(wmNews);
            return false;
        }
//        6. 如果没有那么直接返回true
        return true;
    }

    @Autowired
    private WmChannelService wmChannelService;

    /**
     * 将news的内容保存到article表中
     * @param wmNews
     */
    private ResponseResult saveToArticle(WmNews wmNews) {

        try {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(wmNews,articleDto);

            articleDto.setAuthorId(wmNews.getUserId());

            WmUser wmUser = wmUserService.getById((Serializable) wmNews.getUserId());
            articleDto.setAuthorName(wmUser.getName());

            articleDto.setId(wmNews.getArticleId());

            WmChannel wmChannel = wmChannelService.getById(wmNews.getChannelId());
            articleDto.setChannelName(wmChannel.getName());

            articleDto.setLayout(wmNews.getType());

            articleDto.setFlag(0);

            return iArticleClient.saveArticle(articleDto);
        } catch (Exception e) {
            e.getStackTrace();
            log.error("参数赋值错误");
            throw new CustomException(AppHttpCodeEnum.SERVER_ERROR);
        }
    }

    /**
     * 对文字和图片进行审核
     * @param textAndImage
     * @return
     */
    private boolean auditTextAndImage(Map<String, Object> textAndImage,WmNews wmNews) {
        boolean isAudit = true;

        try {
            if(textAndImage==null){
                return true;
            }
            String texts = (String) textAndImage.get("texts");
            List<String> images = (List<String>) textAndImage.get("images");

//        审核文字
            Map map = textAudit.auditTest(texts);
//        如果返回不是审核通过，那么需要直接更新数据库表
            if("0".equals(map.get("status"))){
                return UpdateNewsStatus(wmNews, map,WmMediaConstans.NEWS_STATUS_FAIL);
            }

//        审核图片
            Map map1 = imageAudit.auditImage(images);
            if("0".equals(map.get("status"))){
                return UpdateNewsStatus(wmNews, map,WmMediaConstans.NEWS_STATUS_FAIL);
            }

//            审核通过，把文章状态改为审核通过但是没发布
            if("200".equals(map.get("status")) && "200".equals(map1.get("status"))){
                UpdateNewsStatus(wmNews,null,WmMediaConstans.NEWS_STATUS_COMMIT_PASS);
            }

            return true;
        }catch (Exception e){
            log.error("图片文字审核错误");
            e.getStackTrace();
            isAudit=false;
        }
        return isAudit;


    }

    //上面审核失败ctril+alt+m抽出出来相同的部分
    private boolean UpdateNewsStatus(WmNews wmNews, Map map,int status) {
        if(status==WmMediaConstans.NEWS_STATUS_COMMIT_PASS){
            wmNews.setStatus(status);
            wmNews.setReason("审核通过");
            wmNewsService.updateById(wmNews);
            return true;
        }
        wmNews.setStatus(status);
        wmNews.setReason((String) map.get("reason"));
        wmNewsService.updateById(wmNews);
        return false;
    }

    /**
     * 获取文章的所有文字和图片并进行审核
     * @param wmNews
     * @return
     */
    private Map<String,Object> getTextAndImage(WmNews wmNews) {
//        参数判断
        if(StringUtils.isBlank(wmNews.getContent()) || StringUtils.isBlank(wmNews.getTitle())){
            log.error("文章标题和内容不能为空");
            throw new CustomException(AppHttpCodeEnum.PARAM_INVALID);
        }

        StringBuffer texts = new StringBuffer();
        List<String> images = new ArrayList<>();

        List<Map> maps = JSON.parseArray(wmNews.getContent(), Map.class);
        for(Map<String,String> map : maps){
            if("text".equals(map.get("type"))){
                texts.append(map.get("value"));
            }

            if("image".equals(map.get("type"))){
                images.add(map.get("value"));
            }
        }

//        文章还需要加上标题
        texts.append("-").append(wmNews.getTitle());
//        图片还要加上封面
        String newsImages = wmNews.getImages();
        Map textResultMap = textAudit.auditTest(texts.toString());
        if(StringUtils.isNotBlank(newsImages)){
            for (String s : newsImages.split(",")) {
                images.add(s);
            }
        }


        HashMap<String, Object> textsAndImages = new HashMap<>();
        textsAndImages.put("texts",texts.toString());
        textsAndImages.put("images",images);
        return textsAndImages;
    }
}
