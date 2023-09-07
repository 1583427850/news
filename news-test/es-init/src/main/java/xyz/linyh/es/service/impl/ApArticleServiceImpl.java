package xyz.linyh.es.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import xyz.linyh.es.mapper.ApArticleMapper;
import xyz.linyh.es.service.ApArticleService;

import org.springframework.stereotype.Service;
import xyz.linyh.model.article.entity.ApArticle;

/**
* @author lin
* @description 针对表【ap_article(文章信息表，存储已发布的文章)】的数据库操作Service实现
* @createDate 2023-08-01 22:10:50
*/
@Service
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>
    implements ApArticleService{

}




