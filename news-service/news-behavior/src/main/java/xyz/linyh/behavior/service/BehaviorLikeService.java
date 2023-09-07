package xyz.linyh.behavior.service;

import xyz.linyh.model.behavior.dto.BehaviorLikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface BehaviorLikeService {

    /**
     * 对某篇文章点赞
     * @return
     */
    public ResponseResult like(BehaviorLikeDto dto);
}
