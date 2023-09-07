package xyz.linyh.essearch.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.essearch.interceptor.SaveUserIdInterceptor;
import xyz.linyh.essearch.service.ApUserSearchService;
import xyz.linyh.model.common.dtos.ResponseResult;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * 获取所有用户的查询记录
     * @return
     */
    @PostMapping("/load")
    public ResponseResult searchAllHistory(){
        Long userId = SaveUserIdInterceptor.AP_USER_THREAD.get();
        return apUserSearchService.findAllHistory(userId);
    }

    /**
     * 删除搜索历史记录
     * @param id
     * @return
     */
    @PostMapping("/del")
    public ResponseResult delSearchHistory(@RequestBody String id){
        Long userId = SaveUserIdInterceptor.AP_USER_THREAD.get();

        Map<String,String> map = JSON.parseObject(id, Map.class);
        return apUserSearchService.deleteSearchHistory(map.get("id"),userId);

    }
}
