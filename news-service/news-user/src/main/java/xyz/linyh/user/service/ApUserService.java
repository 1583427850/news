package xyz.linyh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.user.dto.LoginDto;
import xyz.linyh.model.user.entity.ApUser;

public interface ApUserService extends IService<ApUser> {

    /**
     * app端用户登录
     * @param loginDto
     * @return
     */
    public ResponseResult login(LoginDto loginDto);

}
