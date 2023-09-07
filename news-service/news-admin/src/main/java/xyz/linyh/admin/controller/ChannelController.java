package xyz.linyh.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.feign.IWmbMediaClient;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;
import xyz.linyh.model.webmedia.dto.WmChannelSaveDto;
import xyz.linyh.model.webmedia.dto.WmChannelUpdateDto;


@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private IWmbMediaClient WmbMediaClient;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody CommonPageDto dto){
        return WmbMediaClient.listChannel(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody WmChannelSaveDto dto){
        return WmbMediaClient.saveWmChannel(dto);
    }

    @PostMapping("/update")
    public ResponseResult updateChannel(@RequestBody WmChannelUpdateDto dto){
        return WmbMediaClient.updateChannel(dto);
    }

    @GetMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return WmbMediaClient.delWmChannel(id);
    }

}
