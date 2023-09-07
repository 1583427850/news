package xyz.linyh.webmedia.controller.v1;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.webmedia.service.WmChannelService;

@RestController
@RequestMapping("/api/v1/channel")
public class ChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @ApiOperation(("获取所有频道"))
    @GetMapping("/channels")
    public ResponseResult channels(){
        return wmChannelService.channels();
    }
}
