package xyz.linyh.article.job;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.security.IdMappingConstant;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.CacheService;
import xyz.linyh.common.constants.ApArticleConstants;
import xyz.linyh.feign.IWmbMediaClient;
import xyz.linyh.model.article.dto.ApArticleHot;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.entity.WmChannel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ComputeArticleScore {

    @Autowired
    private ApArticleService apArticleService;

    @Autowired
    private IWmbMediaClient wmbMediaClient;

    @Autowired
    private CacheService cacheService;

    /**
     * 根据文章点赞阅读收藏不同的权重计算文章的总权重
     * 阅读权重为1 点赞权重为3 收藏权重为5
     */
    @XxlJob("computeHostArticle")
    public void compute(){
//        1. 获取前7天的所有文章
        Date date = DateTime.now().minusDays(7).toDate();
        List<ApArticle> apArticles = apArticleService.list(Wrappers.<ApArticle>lambdaQuery().gt(ApArticle::getPublishTime, date));

        if(apArticles==null || apArticles.size() == 0){
            return;
        }

//        2. 更具文章里面的点赞阅读收藏计算总权重
        List<ApArticleHot> apArticleHots = new ArrayList<>();
        for (ApArticle apArticle : apArticles) {
            Integer score = 0;
            if(apArticle.getViews()!=null){
                score+=apArticle.getViews();
            }
            if(apArticle.getLikes()!=null){
                score+=apArticle.getLikes() * ApArticleConstants.ARTICLE_LIKE_WEIGHT;
            }
            if(apArticle.getCollection()!=null){
                score+=apArticle.getCollection() * ApArticleConstants.ARTICLE_COLLECTION_WEIGHT;
            }

            ApArticleHot apArticleHot = new ApArticleHot();
            BeanUtils.copyProperties(apArticle,apArticleHot);
            apArticleHot.setScore(score);
            apArticleHots.add(apArticleHot);
        }

        List<ApArticleHot> sortArticleHots = apArticleHots.stream().sorted(Comparator.comparing(ApArticleHot::getScore).reversed()).collect(Collectors.toList());
        if(sortArticleHots.size()>30) sortArticleHots.subList(0,30);
        cacheService.set(ApArticleConstants.ARTICLE_HOT_INDEX_+ ApArticleConstants.DEFAULT_TAG, JSON.toJSONString(sortArticleHots));

//        3. 将文章根据不同的频道分类
        ResponseResult responseResult = wmbMediaClient.listAllChannel();
        if(responseResult.getCode()!=200 || responseResult.getData()==null){
            return;
        }
        String str = JSON.toJSONString(responseResult.getData());
        List<WmChannel> wmChannels = JSON.parseArray(str, WmChannel.class);



        for (WmChannel wmChannel : wmChannels) {
//            过滤出所有文章里面是这个频道的所有文章
            List<ApArticleHot> collect = apArticleHots.stream().filter(o -> o.getChannelId() == wmChannel.getId()).collect(Collectors.toList());

//            对里面的文章进行排序
            List<ApArticleHot> collect1 = collect.stream().sorted(Comparator.comparing(ApArticleHot::getScore).reversed()).collect(Collectors.toList());
//            判断文章数量是否超出30条，如果超出30条，那么截断
            if(collect1.size()>30) collect1.subList(0,30);

//            将文章保存到redis中
            cacheService.set(ApArticleConstants.ARTICLE_HOT_INDEX_+wmChannel.getId(),JSON.toJSONString(collect1));
        }

        log.info("热度文章计算完成");


    }
}
