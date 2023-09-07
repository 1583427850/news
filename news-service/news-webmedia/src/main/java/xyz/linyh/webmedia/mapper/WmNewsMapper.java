package xyz.linyh.webmedia.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.webmedia.entity.WmNews;

/**
* @author lin
* @description 针对表【wmb_news(自媒体图文内容信息表)】的数据库操作Mapper
* @createDate 2023-07-11 22:30:52
* @Entity generator.entity.WmbNews
*/
@Mapper
public interface WmNewsMapper extends BaseMapper<WmNews> {

}




