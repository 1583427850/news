package xyz.linyh.essearch.service;

import xyz.linyh.essearch.dto.ApUserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;

//用来操作用户搜索相关数据
public interface ApUserSearchService {

    /**
     * 用来保存用户搜索记录
     * @param userId
     * @param searchWorld
     * @return
     */
    void insertHistory(Long userId,String searchWorld);

    /**
     * 查找这个用户所有的搜索记录
     * @param userId
     * @return
     */
    ResponseResult findAllHistory(Long userId);

    /**
     * 删除这个用户历史搜索记录
     * @param id
     * @return
     */
    ResponseResult deleteSearchHistory(String id,Long userId);
}
