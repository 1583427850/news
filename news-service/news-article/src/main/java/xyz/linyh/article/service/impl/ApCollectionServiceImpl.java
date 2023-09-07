package xyz.linyh.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.linyh.article.interceptor.GetIdInterceptor;
import xyz.linyh.article.mapper.ApCollectionMapper;
import xyz.linyh.article.service.ApArticleService;
import xyz.linyh.article.service.ApCollectionService;
import xyz.linyh.model.article.dto.ApCollectionDto;
import xyz.linyh.model.article.entity.ApArticle;
import xyz.linyh.model.article.entity.ApCollection;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.common.enums.AppHttpCodeEnum;

import java.util.Date;

/**
* @author lin
* @description 针对表【ap_collection(APP收藏信息表)】的数据库操作Service实现
* @createDate 2023-08-16 10:18:25
*/
@Service
public class ApCollectionServiceImpl extends ServiceImpl<ApCollectionMapper, ApCollection>
    implements ApCollectionService {

    @Autowired
    private ApArticleService apArticleService;

    /**
     * 对文章收藏操作
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult collection(ApCollectionDto dto) {
//        0. 参数校验
        if(dto==null || dto.getEntryId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }

//        1. 收藏
        ApArticle apArticle = apArticleService.getOne(Wrappers.<ApArticle>lambdaQuery().eq(ApArticle::getId, dto.getEntryId()));
        if(apArticle.getCollection()==null){
            apArticle.setCollection(0);
        }
        Long userId = GetIdInterceptor.AP_THREAD.get();
        if(dto.getOperation()==0){
            ApCollection collection = getOne(Wrappers.<ApCollection>lambdaQuery().eq(ApCollection::getArticleId, dto.getEntryId()).eq(ApCollection::getEntryId, userId));
            if(collection!=null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"已经收藏");
            }

            ApCollection apCollection = new ApCollection();
            apCollection.setArticleId(dto.getEntryId());
            apCollection.setEntryId(userId);
            apCollection.setCollectionTime(new Date());
            apCollection.setPublishedTime(dto.getPublishedTime());
            apCollection.setType(dto.getType());
            save(apCollection);

//            文章收藏数量加1
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,dto.getEntryId()).set(ApArticle::getCollection,apArticle.getCollection()+1));
        }else{
//            取消收藏
            remove(Wrappers.<ApCollection>lambdaQuery().eq(ApCollection::getEntryId,userId).eq(ApCollection::getArticleId,dto.getEntryId()));
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,dto.getEntryId()).set(ApArticle::getCollection,apArticle.getCollection()-1));
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}




