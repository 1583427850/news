package xyz.linyh.user.service;

import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.user.dto.UserFollowDto;

public interface UserFollowService {

    /**
     * 读者关注或取消关注
     * @param dto
     * @return
     */
    ResponseResult follow(UserFollowDto dto);
}
