package xyz.linyh.webmedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmNewsDto;
import xyz.linyh.model.webmedia.dto.WmNewsListDto;
import xyz.linyh.model.webmedia.dto.WmUpOrDownDto;
import xyz.linyh.model.webmedia.entity.WmNews;

/**
* @author lin
* @description 针对表【wmb_news(自媒体图文内容信息表)】的数据库操作Service
* @createDate 2023-07-11 22:30:52
*/
@Mapper
public interface WmNewsService extends IService<WmNews> {

    /**
     * 条件查询所有文章
     * @param dto
     * @return
     */
    public ResponseResult listAllNews(WmNewsListDto dto);

    /**
     * 新增或存草稿文章
     * @param dto
     * @return
     */
    ResponseResult submitOrSubmitDraftNew(WmNewsDto dto);

    /**
     * 从redis中获取待消费的任务
     * @return
     */
    void getTasksToCache();

    /**
     * 对媒体端的文章进行上下架操作
     * @param dto
     * @return
     */
    ResponseResult upOrDownNews(WmUpOrDownDto dto);
}
