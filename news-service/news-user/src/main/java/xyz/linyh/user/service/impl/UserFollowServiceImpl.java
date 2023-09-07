package xyz.linyh.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.common.constants.UserConstants;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.user.dto.UserFollowDto;
import xyz.linyh.user.interceptor.GetIdInterceptor;
import xyz.linyh.user.service.CacheService;
import xyz.linyh.user.service.UserFollowService;

@Service
@Slf4j
public class UserFollowServiceImpl implements UserFollowService {


    @Autowired
    private CacheService cacheService;
    /**
     * 读者关注或取消关注
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult follow(UserFollowDto dto) {
//        0. 参数校验
        if(dto==null || dto.getArticleId()==null || dto.getAuthorId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

        Long userId = GetIdInterceptor.USER_THREAD.get();

        if(dto.getAuthorId()==userId){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"自己不能关注自己");
        }


        String followKey = UserConstants.USER_FOLLOW_PRE+userId;
        String fanKey = UserConstants.USER_FAN_PRE+dto.getAuthorId();
        if(dto.getOperation()==0){
//        1. 关注
            Object o = cacheService.hGet(followKey, String.valueOf(userId));
            if(o!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"你已经是他的粉丝了");
            }
//            将用户的关注列表里面添加文章作者
            cacheService.hPut(followKey, String.valueOf(dto.getAuthorId()),"关注");
//            将作者的粉丝列表添加这个用户
            cacheService.hPut(fanKey, String.valueOf(userId),"粉丝");
        }else {
//        2.取消关注
            cacheService.hDelete(followKey,String.valueOf(dto.getAuthorId()));
            cacheService.hDelete(fanKey,String.valueOf(userId));
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
