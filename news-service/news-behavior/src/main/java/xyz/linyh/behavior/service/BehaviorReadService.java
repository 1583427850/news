package xyz.linyh.behavior.service;

import xyz.linyh.model.behavior.dto.BehaviorReadDto;
import xyz.linyh.model.common.dtos.ResponseResult;

public interface BehaviorReadService {

    /**
     * 添加文章阅读数据
     * @param dto
     * @return
     */
    ResponseResult read(BehaviorReadDto dto);
}
