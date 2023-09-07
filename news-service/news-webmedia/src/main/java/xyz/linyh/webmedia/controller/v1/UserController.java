package xyz.linyh.webmedia.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmLoginDto;
import xyz.linyh.webmedia.service.WmUserService;

@RestController
@RequestMapping("")
public class UserController {

    @RestController
    @RequestMapping("/login")
    public class LoginController {

        @Autowired
        private WmUserService wmUserService;

        @PostMapping("/in")
        public ResponseResult login(@RequestBody WmLoginDto dto){
            return wmUserService.login(dto);
        }
    }
}
