package xyz.linyh.webmedia.feign;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.feign.IWmbMediaClient;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.dto.WmChannelSaveDto;
import xyz.linyh.model.webmedia.dto.WmChannelUpdateDto;
import xyz.linyh.webmedia.service.WmChannelService;
import xyz.linyh.webmedia.service.WmSensitiveService;

import java.util.Map;

@RestController
public class WmbMediaClient implements IWmbMediaClient {

    @Autowired
    private WmChannelService wmChannelService;

    @Autowired
    private WmSensitiveService wmSensitiveService;

    /**
     * 删除敏感词
     *
     * @param id
     * @return
     */
    @Override
    @DeleteMapping("/api/v1/sensitive/del/{id}")
    public ResponseResult delSensitive(@PathVariable Long id) {
        return wmSensitiveService.delSensitiveById(id);
    }

    /**
     * 修改敏感词
     *
     * @param map
     * @return
     */
    @Override
    @PostMapping("/api/v1/sensitive/update")
    public ResponseResult updateSensitive(@RequestBody Map<String, String> map) {
        return wmSensitiveService.updateSensitive(map.get("id"),map.get("sensitives"));
    }

    /**
     * 新增敏感词
     *
     * @param map
     * @return
     */
    @Override
    @PostMapping("/api/v1/sensitive/save")
    public ResponseResult saveSensitive(Map<String,String> map) {
        return wmSensitiveService.saveSensitive(map.get("sensitives"));
    }

    /**
     * 查询所有敏感词
     *
     * @param dto
     * @return
     */
    @Override
    @PostMapping("/api/v1/sensitive/list")
    public ResponseResult listSensitive(@RequestBody CommonPageDto dto) {
        return wmSensitiveService.listAllSensitiveByPage(dto);
    }

    @ApiOperation(("分页获取所有频道"))
    @PostMapping("/api/v1/channel/list")
    public ResponseResult listChannel(@RequestBody CommonPageDto dto){
        return wmChannelService.listAllChanelByPage(dto);
    }

    @Override
    @GetMapping("/api/v1/channel/listAll")
    public ResponseResult listAllChannel() {
        return wmChannelService.channels();
    }

    @Override
    @PostMapping("/api/v1/channel/save")
    public ResponseResult saveWmChannel(@RequestBody WmChannelSaveDto dto) {
        return wmChannelService.saveChannel(dto);
    }

    @Override
    @GetMapping("/api/v1/channel/del/{id}")
    public ResponseResult delWmChannel(@PathVariable Long id) {
        return wmChannelService.delChannelById(id);
    }

    @Override
    public ResponseResult updateChannel(WmChannelUpdateDto dto) {
        return wmChannelService.updateChannel(dto);
    }
}
