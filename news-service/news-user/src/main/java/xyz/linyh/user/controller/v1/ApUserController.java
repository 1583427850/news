package xyz.linyh.user.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.user.dto.LoginDto;
import xyz.linyh.user.service.ApUserService;

@Api(value = "app端用户登录",tags = "app端用户登录")
@RestController
@RequestMapping("/api/v1")
public class ApUserController {

    @Autowired
    private ApUserService apUserService;

    @ApiOperation("用户登录")
    @PostMapping("/login/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto){
        return apUserService.login(loginDto);
    }
}
