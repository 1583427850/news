package xyz.linyh.user.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.user.dto.UserFollowDto;
import xyz.linyh.user.service.UserFollowService;

@RestController
@RequestMapping("/api/v1/user")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @PostMapping("/user_follow")
    public ResponseResult UserFollow(@RequestBody UserFollowDto dto){
        return userFollowService.follow(dto);
    }
}
