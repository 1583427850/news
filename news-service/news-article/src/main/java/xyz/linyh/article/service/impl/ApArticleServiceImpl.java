package xyz.linyh.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.article.mapper.ApArticleMapper;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.common.constants.ApArticleConstants;
import xyz.linyh.model.article.dto.ArticleHomeDto;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.common.dtos.ResponseResult;

import java.util.Date;
import java.util.List;

@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;


    /**
     * 查询文章概述
     *
     * @param dto
     * @param type 1为查询更多 2为加载最新
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, int type) {
        //参数校验
//        1. 时间校验
        if(dto.getMaxBehotTime()==null){
            dto.setMaxBehotTime(new Date());
        }
        if(dto.getMinBehotTime()==null){
            dto.setMinBehotTime(new Date());
        }
//        2.size校验
        if(dto.getSize()==null){
            dto.setSize(ApArticleConstants.DEFAULT_PAGE_SIZE);
        }
        if(dto.getSize()>ApArticleConstants.MAX_PAGE_SIZE){
            dto.setSize(ApArticleConstants.MAX_PAGE_SIZE);
        }

//        3. 频道id校验
        if(dto.getTag()==null){
            dto.setTag(ApArticleConstants.DEFAULT_TAG);
        }
//        4. type校验 因为初始化查询也是根据最大时间查询的，所以如果是初始化查询，就是直接是type传入1
        if(type!=ApArticleConstants.LOAD_MORE_TYPE && type!=ApArticleConstants.LOAD_NEWS_TYPE){
            type=ApArticleConstants.LOAD_MORE_TYPE;
        }

        List<ApArticle> load = apArticleMapper.load(dto, type);
        return ResponseResult.okResult(load);
    }
}
