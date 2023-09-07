package xyz.linyh.behavior.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.behavior.service.BehaviorReadService;
import xyz.linyh.behavior.service.CacheService;
import xyz.linyh.common.constants.BehaviorConstants;
import xyz.linyh.model.behavior.dto.BehaviorReadDto;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

@Service
@Slf4j
public class BehaviorReadServiceImpl implements BehaviorReadService {

    @Autowired
    private CacheService cacheService;

    /**
     * 添加文章阅读数据
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult read(BehaviorReadDto dto) {
//        0. 参数校验
        if(dto==null || dto.getArticleId() ==null ){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//        1. 阅读
        String key = BehaviorConstants.AP_BEHAVIOR_READ_PRE + dto.getArticleId();
        String preCount = cacheService.get(key);

        if(preCount!=null) {
            int preCountI = Integer.parseInt(preCount);
            dto.setCount(dto.getCount() + preCountI);
        }
        cacheService.set(key, String.valueOf(dto.getCount()));


        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
