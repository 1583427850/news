package xyz.linyh.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import xyz.linyh.model.article.dto.ApCollectionDto;
import xyz.linyh.model.article.entity.ApCollection;
import xyz.linyh.model.common.dtos.ResponseResult;

/**
* @author lin
* @description 针对表【ap_collection(APP收藏信息表)】的数据库操作Service
* @createDate 2023-08-16 10:18:25
*/
public interface ApCollectionService extends IService<ApCollection> {

    /**
     * 对文章收藏
     * @param dto
     * @return
     */
    ResponseResult collection(ApCollectionDto dto);
}
