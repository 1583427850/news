package xyz.linyh.es.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.vo.SearchArticleVo;

import java.util.List;

/**
* @author lin
* @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Mapper
* @createDate 2023-08-01 22:10:50
* @Entity generator.entity.ApArticle
*/

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    List<SearchArticleVo> loadArticleList();

}




