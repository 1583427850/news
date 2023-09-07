package xyz.linyh.article.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.CacheService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.model.article.entity.ApArticle;

import java.util.Set;

@Component
@Slf4j
public class ComputeBehavior {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ApArticleService apArticleService;

    @XxlJob("ComputedBehavior")
    public void compute(){
        updateLike();
        updateRead();
        log.info("文章喜欢阅读计算完成。。。。");
    }

    /**
     * 更新mysql文章点赞数据
     */
    private void updateRead() {
        Set<String> reads = cacheService.scan(BehaviorConstants.AP_BEHAVIOR_READ_PRE + "*");
        for (String read : reads) {
            String[] s = read.split("_");
            String readNum = cacheService.get(read);

            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,s[s.length-1]).set(ApArticle::getViews,Integer.parseInt(readNum)));
        }
    }

    /**
     * 更新mysql文章点赞数据
     */
    private void updateLike() {
        Set<String> likes = cacheService.scan(BehaviorConstants.AP_BEHAVIOR_LIKE_PRE + "*");
        for (String like : likes) {
            String[] s = like.split("_");
            Long likeNum = cacheService.hSize(like);
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,s[s.length-1]).set(ApArticle::getLikes,likeNum));
        }
    }
}
