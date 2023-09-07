package xyz.linyh.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;
import xyz.linyh.feign.IWmbMediaClient;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.CommonPageDto;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/sensitive")
public class SensitiveController {

    @Autowired
    private IWmbMediaClient wmbMediaClient;

    @PostMapping("/list")
    public ResponseResult list(@RequestBody CommonPageDto dto){
        return wmbMediaClient.listSensitive(dto);
    }

    @PostMapping("/save")
    public ResponseResult save(@RequestBody Map<String,String> map){
        return wmbMediaClient.saveSensitive(map);
    }

    @PostMapping("/update")
    public ResponseResult update(@RequestBody Map<String,String> map){
        return wmbMediaClient.updateSensitive(map);
    }

    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable Long id){
        return wmbMediaClient.delSensitive(id);
    }
}
