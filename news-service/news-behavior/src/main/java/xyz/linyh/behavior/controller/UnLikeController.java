package xyz.linyh.behavior.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.behavior.service.BehaviorUnlikeService;
import xyz.linyh.model.behavior.dto.BehaviorLikeDto;
import xyz.linyh.model.behavior.dto.BehaviorUnlikeDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1")
public class UnLikeController {

    @Autowired
    private BehaviorUnlikeService behaviorUnlikeService;

    @PostMapping("/un_likes_behavior")
    public ResponseResult unLike(@RequestBody BehaviorUnlikeDto dto){
        return behaviorUnlikeService.unlike(dto);
    }
}
