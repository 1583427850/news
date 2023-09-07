package xyz.linyh.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.article.interceptor.GetIdInterceptor;
import xyz.linyh.article.service.ApCollectionService;
import xyz.linyh.article.service.CacheService;
import xyz.linyh.article.service.LoadArticleBehaviorService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.common.constants.UserConstants;
import xyz.linyh.model.article.dto.LoadArticleBehaviorDto;
import xyz.linyh.model.article.entity.ApCollection;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.HashMap;

@Service
@Slf4j
public class LoadArticleBehaviorServiceImpl implements LoadArticleBehaviorService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ApCollectionService apCollectionService;
    /**
     * 加载某个用户文章的行为
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult load(LoadArticleBehaviorDto dto) {
//        0. 参数校验
        if(dto==null || dto.getArticleId()==null || dto.getAuthorId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        HashMap<String, Object> result = new HashMap<>();
        Long userId = GetIdInterceptor.AP_THREAD.get();
//        1. 获取点赞信息
        String likeKey = BehaviorConstants.AP_BEHAVIOR_LIKE_PRE+dto.getArticleId();
        Object likeObj = cacheService.hGet(likeKey, String.valueOf(userId));
        Boolean islike = false;
        if(likeObj!=null) {
            islike = true;
        }
        result.put("islike",islike);

//        2. 获取不喜欢信息
        String unlikeKey = BehaviorConstants.AP_BEHAVIOR_UN_LIKE_PRE+dto.getArticleId();
        Object unlikeObj = cacheService.hGet(unlikeKey, String.valueOf(userId));
        Boolean isunlike = false;
        if(unlikeObj!=null){
            isunlike=true;
        }
        result.put("isunlike",isunlike);

//        3. 获取关注信息
        String followKey = UserConstants.USER_FOLLOW_PRE+userId;
        Object followObj = cacheService.hGet(followKey, String.valueOf(dto.getAuthorId()));
        Boolean isfollow = false;
        if(followObj!=null){
            isfollow=true;
        }
        result.put("isfollow",isfollow);

//        4. 获取收藏信息
        ApCollection apCollection = apCollectionService.getOne(Wrappers.<ApCollection>lambdaQuery().eq(ApCollection::getArticleId, dto.getArticleId()).eq(ApCollection::getEntryId, userId));
        Boolean isCollection = false;
        if(apCollection!=null){
            isCollection=true;
        }
        result.put("iscollection",isCollection);

        return ResponseResult.okResult(result);
    }
}
