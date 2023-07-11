package xyz.linyh.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.net.nntp.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.linyh.model.article.dto.ArticleHomeDto;
import xyz.linyh.model.article.entity.ApArticle;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 查询文章
     * @param dto
     * @param type 1为查询更多 2为加载最新
     * @return
     */
    public List<ApArticle> load(@Param("dto") ArticleHomeDto dto, @Param("type") int type);
}
