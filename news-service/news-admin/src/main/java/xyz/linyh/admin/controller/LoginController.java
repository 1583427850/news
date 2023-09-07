package xyz.linyh.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.admin.service.AdUserService;
import xyz.linyh.model.admin.dto.LoginDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdUserService adUserService;


    @PostMapping("/in")
    public ResponseResult login(@RequestBody LoginDto dto){
        return adUserService.login(dto);
    }
}
