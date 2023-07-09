package xyz.linyh.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;
import xyz.linyh.model.user.dto.LoginDto;
import xyz.linyh.model.user.entity.ApUser;
import xyz.linyh.user.mapper.ApUserMapper;
import xyz.linyh.user.service.ApUserService;
import xyz.linyh.utils.common.AppJwtUtil;

import java.util.HashMap;

@Service
@Transactional
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Autowired
    private ApUserMapper apUserMapper;

    /**
     * app端用户登录
     *
     * @param loginDto
     * @return
     */
    @Override
    public ResponseResult login(LoginDto loginDto) {

//        1. 判断用户是否输入手机号和密码
        if (StringUtils.isNotBlank(loginDto.getPhone()) && StringUtils.isNotBlank(loginDto.getPassword())) {
//            2. 如果输入密码了，那么开始验证手机号和密码是否正确
//                 2.1 根据手机号码去数据库获取对应的盐和密码
            ApUser user = apUserMapper.selectOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, loginDto.getPhone()));
//                 2.2 如果找不到对应内容，那么就直接登录失败
            if(user==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"手机号不存在");
            }
//                2.3 找到对应内容，那么就将用户输入的密码和数据库里面的盐合并然后加密，加密后和数据库的密码进行对比
            String EncodeDtoPassword = DigestUtils.md5DigestAsHex((loginDto.getPassword() + user.getSalt()).getBytes());
            if(!EncodeDtoPassword.equals(user.getPassword())){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
//                2.4 正确就登录成功，返回以用户id生成的token和ApUser对象
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", AppJwtUtil.getToken(user.getId()));
            user.setSalt("");
            user.setPassword("");
            map.put("user",user);
            return ResponseResult.okResult(map);

        }
//        3. 如果没输入，那么就直接游客登录，返回的token是以id为0生成的
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",AppJwtUtil.getToken(0L));
        return ResponseResult.okResult(map);

    }
}
