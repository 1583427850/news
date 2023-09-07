package xyz.linyh.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.admin.dto.entity.AdUser;

/**
* @author lin
* @description 针对表【ad_user(管理员用户信息表)】的数据库操作Mapper
* @createDate 2023-08-11 23:41:37
* @Entity generator.entity.AdUser
*/

@Mapper
public interface AdUserMapper extends BaseMapper<AdUser> {

}




