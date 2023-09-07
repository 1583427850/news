package xyz.linyh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.dto.WmChannelSaveDto;
import xyz.linyh.model.webmedia.dto.WmChannelUpdateDto;

import java.util.Map;

@FeignClient(value = "news-webmedia")
public interface IWmbMediaClient {

    /**
     * 删除敏感词
     * @param id
     * @return
     */
    @DeleteMapping("/api/v1/sensitive/del/{id}")
    ResponseResult delSensitive(@PathVariable("id") Long id);

    /**
     * 修改敏感词
     * @param map
     * @return
     */
    @PostMapping("/api/v1/sensitive/update")
    ResponseResult updateSensitive(@RequestBody Map<String, String> map);

    /**
     * 新增敏感词
     * @param map
     * @return
     */
    @PostMapping("/api/v1/sensitive/save")
    public ResponseResult saveSensitive(@RequestBody Map<String,String> map);

    /**
     * 查询所有敏感词
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult listSensitive(@RequestBody CommonPageDto dto);

    /**
     * 查询所有的频道
     * @return
     */
    @PostMapping("/api/v1/channel/list")
    public ResponseResult listChannel(@RequestBody CommonPageDto dto);

    @GetMapping("/api/v1/channel/listAll")
    public ResponseResult listAllChannel();

    /**
     * 新增频道
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/channel/save")
    public ResponseResult saveWmChannel(@RequestBody WmChannelSaveDto dto);

    /**
     * 删除频道
     * @param id
     * @return
     */
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult delWmChannel(@PathVariable Long id);

    /**
     * 更新频道
     * @param dto
     * @return
     */
    @PostMapping("/api/v1/channel/update")
    public ResponseResult updateChannel(@RequestBody WmChannelUpdateDto dto);


}
