package xyz.linyh.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.cache.CachesEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.article.mapper.ApArticleMapper;
import xyz.linyh.article.service.*;
import xyz.linyh.common.constants.ApArticleConstants;
import xyz.linyh.model.article.dto.*;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApArticleConfig;
import xyz.linyh.model.article.entity.ApArticleContent;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleContentService apArticleContentService;

    @Autowired
    private ApArticleConfigService aparticleConfigService;


    /**
     * 查询文章概述
     *
     * @param dto
     * @param type 1为查询更多 2为加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, int type) {
        //参数校验
//        1. 时间校验
        if(dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        if(dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }
//        2.size校验
        if(dto.getSize()==null){
            dto.setSize(ApArticleConstants.DEFAULT_PAGE_SIZE);
        }
        if(dto.getSize()>ApArticleConstants.MAX_PAGE_SIZE){
            dto.setSize(ApArticleConstants.MAX_PAGE_SIZE);
        }

//        3. 频道id校验
        if(dto.getTag()==null){
            dto.setTag(ApArticleConstants.DEFAULT_TAG);
        }
//        4. type校验 因为初始化查询也是根据最大时间查询的，所以如果是初始化查询，就是直接是type传入1
        if(type!=ApArticleConstants.LOAD_MORE_TYPE && type!=ApArticleConstants.LOAD_NEWS_TYPE){
            type=ApArticleConstants.LOAD_MORE_TYPE;
        }

        List<ApArticle> load = apArticleMapper.load(dto, type);
        return ResponseResult.okResult(load);
    }

    @Autowired
    private CacheService cacheService;

    /**
     * 加载文章，优先加载热度较高的文章
     *
     * @param dto
     * @param type
     * @param isHome
     * @return
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, int type, Boolean isHome) {
//        0. 参数校验
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//        1. 判断是否是初始页面
        if(!isHome){
//        2. 如果不是初始页面，那么就不同根据热度查询了，直接根据时间查询
            return load(dto,type);
        }

//        3. 如果是初始页面，那么需要去redis中获取对应频道的热度文章
        String str = cacheService.get(ApArticleConstants.ARTICLE_HOT_INDEX_ + dto.getTag());
        if(StringUtils.isBlank(str)){
            return load(dto,type);
        }

        List<ApArticleHot> apArticleHots = JSON.parseArray(str, ApArticleHot.class);

        if(apArticleHots.size()<10){
            ResponseResult load = load(dto, type);
            String jsonString = JSON.toJSONString(load.getData());
            List<ApArticle> apArticles = JSON.parseArray(jsonString, ApArticle.class);
            for (ApArticle apArticle : apArticles) {
                if(NotIn(apArticle,apArticleHots)){
                    ApArticleHot apArticleHot = new ApArticleHot();
                    BeanUtils.copyProperties(apArticle,apArticleHot);
                    apArticleHots.add(apArticleHot);
                }
            }

        }
//        4. 返回结果
        return ResponseResult.okResult(apArticleHots);
    }

    /**
     * 判断apArticleHots里面是否不存在apArticle
     * @param apArticle
     * @param apArticleHots
     * @return
     */
    private boolean NotIn(ApArticle apArticle, List<ApArticleHot> apArticleHots) {
        boolean flag = true;
        for (ApArticleHot apArticleHot : apArticleHots) {
            if(apArticleHot.getId().equals(apArticle.getId())) flag = false;
        }
        return flag;
    }

    /**
     * 保存或修改app端的文章
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveOrUpdateArticle(ArticleDto dto) {
//        1. 参数校验
        if(dto==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(StringUtils.isBlank(dto.getContent())|| StringUtils.isBlank(dto.getTitle())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//        2. 根据是否有文章id判断是修改文章还是新增文章
        Long articleId = dto.getId();
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);
        if(articleId==null){
//            新增app的文章
            save(apArticle);

//            新增文章内容表
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentService.save(apArticleContent);

//            新增文章配置表
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setArticleId(apArticle.getId());
            apArticleConfig.setIsComment(1);
            apArticleConfig.setIsDelete(0);
            apArticleConfig.setIsDown(0);
            apArticleConfig.setIsForward(1);
            aparticleConfigService.save(apArticleConfig);
        }else{
//            3. 如果是修改文章，那么只需要修改文章和文章内容，不需要修改文章配置（因为原先的文章配置表里面的东西可能还存在修改)
            updateById(apArticle);
            apArticleContentService.update(Wrappers.<ApArticleContent>lambdaUpdate()
                    .eq(ApArticleContent::getArticleId,dto.getId())
                    .set(ApArticleContent::getContent,dto.getContent()));
        }

//        异步调用将文件内容生成静态文件
        saveArticleContentStatic(apArticle.getId());
//            4. 返回修改或新增后的文章id
        return ResponseResult.okResult(apArticle.getId());
    }






    @Autowired
    private FreemarkerService freemarkerService;

    /**
     * 将文章内容生成静态化文件，然后保存到minio中
     * @param apArticleId
     */
    private void saveArticleContentStatic(Long apArticleId) {
        freemarkerService.saveArticleContentToMinIO(apArticleId);

    }
}
