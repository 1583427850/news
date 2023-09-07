package xyz.linyh.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.admin.dto.LoginDto;
import xyz.linyh.model.admin.dto.entity.AdUser;
import xyz.linyh.model.common.dtos.ResponseResult;

/**
* @author lin
* @description 针对表【ad_user(管理员用户信息表)】的数据库操作Service
* @createDate 2023-08-11 23:41:37
*/
public interface AdUserService extends IService<AdUser> {

    /**
     * admin端用户登录
     * @param dto
     * @return
     */
    ResponseResult login(LoginDto dto);
}
