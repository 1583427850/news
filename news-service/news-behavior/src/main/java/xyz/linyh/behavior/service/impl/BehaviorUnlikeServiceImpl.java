package xyz.linyh.behavior.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.behavior.interceptor.GetIdInterceptor;
import xyz.linyh.behavior.service.BehaviorUnlikeService;
import xyz.linyh.behavior.service.CacheService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.model.behavior.dto.BehaviorUnlikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

@Service
@Slf4j
public class BehaviorUnlikeServiceImpl implements BehaviorUnlikeService {

    @Autowired
    private CacheService cacheService;


    /**
     * 对文章不喜欢操作
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult unlike(BehaviorUnlikeDto dto) {
//        0. 参数校验
        if(dto==null || dto.getArticleId() ==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 不喜欢
        String key = BehaviorConstants.AP_BEHAVIOR_UN_LIKE_PRE+dto.getArticleId();
        Long userId = GetIdInterceptor.AP_USER_THREAD.get();

//        判断是否已经不喜欢了
        if(dto.getType()==0){
            Object o = cacheService.hGet(key, String.valueOf(userId));
            if(o!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
            }
            cacheService.hPut(key,String.valueOf(userId),"不喜欢");
        }else{
//            删除不喜欢数据
            cacheService.hDelete(key,String.valueOf(userId));
        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
