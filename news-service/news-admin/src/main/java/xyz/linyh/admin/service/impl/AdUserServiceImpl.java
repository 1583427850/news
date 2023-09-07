package xyz.linyh.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import xyz.linyh.admin.mapper.AdUserMapper;
import xyz.linyh.admin.service.AdUserService;
import xyz.linyh.model.admin.dto.LoginDto;
import xyz.linyh.model.admin.dto.entity.AdUser;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.utils.common.AppJwtUtil;
import xyz.linyh.utils.common.Base64Utils;

import java.util.HashMap;

/**
* @author lin
* @description 针对表【ad_user(管理员用户信息表)】的数据库操作Service实现
* @createDate 2023-08-11 23:41:37
*/
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser>
    implements AdUserService {

    @Autowired
    private AdUserMapper adUserMapper;


    /**
     * admin端用户登录
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto dto) {
//        0. 参数校验
        if(dto==null || StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 判断用户是否存在
        AdUser user = adUserMapper.selectOne(Wrappers.<AdUser>lambdaQuery().eq(AdUser::getName, dto.getName()));
        if(user==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

//        2. 判断密码是否正确
        String salt = user.getSalt();
        String pswd = dto.getPassword();
        pswd = DigestUtils.md5DigestAsHex((pswd + salt).getBytes());
        if(!pswd.equals(user.getPassword())){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

//        3. 如果正确了返回token
        String token = AppJwtUtil.getToken(user.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        user.setId(null);
        user.setPassword(null);
        user.setSalt(null);
        map.put("user",user);
        return ResponseResult.okResult(map);
    }
}




