package xyz.linyh.behavior.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.behavior.interceptor.GetIdInterceptor;
import xyz.linyh.behavior.service.BehaviorLikeService;
import xyz.linyh.behavior.service.CacheService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.model.behavior.dto.BehaviorLikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

@Service
@Slf4j
public class BehaviorLikeServiceImpl implements BehaviorLikeService {

    @Autowired
    private CacheService cacheService;

    /**
     * 对某篇文章点赞
     *
     * @return
     */
    @Override
    public ResponseResult like(BehaviorLikeDto dto) {
//        0. 参数校验
        if(dto==null || dto.getArticleId()==null ){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 点赞(点赞数据先保存到redis中，每一个文章有一个key，用hash结构存储，这样子可以最快找到某个用户是否点赞)
        Long userId = GetIdInterceptor.AP_USER_THREAD.get();
        String key = BehaviorConstants.AP_BEHAVIOR_LIKE_PRE + dto.getArticleId();

        if(dto.getOperation()==0){
//            判断用户是否已经点赞
            Object o = cacheService.hGet(key, String.valueOf(userId));
            if(o!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"不需要重复点赞");
            }

//            新增点赞数据
            cacheService.hPut(key, String.valueOf(userId),"点赞");

        }else{
//            删除点赞数据
            cacheService.hDelete(key,String.valueOf(userId));
        }

        return ResponseResult.okResult(null);
    }
}
