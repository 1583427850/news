package xyz.linyh.behavior.service;

import xyz.linyh.model.behavior.dto.BehaviorUnlikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface BehaviorUnlikeService {

    /**
     * 对文章不喜欢操作
     * @param dto
     * @return
     */
    ResponseResult unlike(BehaviorUnlikeDto dto);
}
