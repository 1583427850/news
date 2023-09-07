package xyz.linyh.webmedia.controller.v1;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.webmedia.dto.WmNewsDto;
import xyz.linyh.model.webmedia.dto.WmNewsListDto;
import xyz.linyh.model.webmedia.dto.WmUpOrDownDto;
import xyz.linyh.webmedia.service.WmNewsService;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @Autowired
    private WmNewsService wmNewsService;

    @ApiOperation("查看所有文章")
    @PostMapping("/list")
    public ResponseResult listAllNews(@RequestBody WmNewsListDto dto){
        return wmNewsService.listAllNews(dto);
    }

    @ApiOperation("新增文章也可以是存草稿")
    @PostMapping("/submit")
    public ResponseResult submitOrSubmitDraftNew(@RequestBody WmNewsDto dto){
        return wmNewsService.submitOrSubmitDraftNew(dto);
    }

    @PostMapping("/down_or_up")
    public ResponseResult upOrDownNews(@RequestBody WmUpOrDownDto dto){
        return ResponseResult.okResult(wmNewsService.upOrDownNews(dto));
    }


}
