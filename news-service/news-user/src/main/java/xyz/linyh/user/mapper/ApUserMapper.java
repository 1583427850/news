package xyz.linyh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.user.entity.ApUser;

@Mapper
public
interface ApUserMapper extends BaseMapper<ApUser> {
}
