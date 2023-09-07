package xyz.linyh.behavior.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.behavior.service.BehaviorLikeService;
import xyz.linyh.model.behavior.dto.BehaviorLikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1")
public class LikeController {

    @Autowired
    private BehaviorLikeService behaviorLikeService;

    /**
     * 用来接收用户点赞请求
     * @param dto
     * @return
     */
    @PostMapping("/likes_behavior")
    public ResponseResult like(@RequestBody BehaviorLikeDto dto){
        return behaviorLikeService.like(dto);
    }


}
