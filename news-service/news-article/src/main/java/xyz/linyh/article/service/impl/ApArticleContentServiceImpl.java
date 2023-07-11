package xyz.linyh.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.linyh.article.mapper.ApArticleContentMapper;
import xyz.linyh.article.service.ApArticleContentService;
import xyz.linyh.model.article.entity.ApArticleContent;

@Transactional
@Service
@Slf4j
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {

}
