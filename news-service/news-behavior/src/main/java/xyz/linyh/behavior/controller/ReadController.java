package xyz.linyh.behavior.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.behavior.service.BehaviorLikeService;
import xyz.linyh.behavior.service.BehaviorReadService;
import xyz.linyh.model.behavior.dto.BehaviorReadDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1")
public class ReadController {

    @Autowired
    private BehaviorReadService behaviorReadService;

    @PostMapping("/read_behavior")
    public ResponseResult readBehavior(@RequestBody BehaviorReadDto dto){
        return behaviorReadService.read(dto);
    }
}
