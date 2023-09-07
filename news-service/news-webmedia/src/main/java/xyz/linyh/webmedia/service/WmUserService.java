package xyz.linyh.webmedia.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmLoginDto;
import xyz.linyh.model.webmedia.entity.WmUser;

/**
* @author lin
* @description 针对表【wmb_user(自媒体用户信息表)】的数据库操作Service
* @createDate 2023-07-11 22:29:53
*/
@Mapper
public interface WmUserService extends IService<WmUser> {

    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}