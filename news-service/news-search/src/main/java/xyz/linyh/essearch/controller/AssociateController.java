package xyz.linyh.essearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.essearch.service.ApAssociateService;
import xyz.linyh.model.article.dto.UserSearchDto;
import xyz.linyh.model.common.dtos.ResponseResult;

@RestController
@RequestMapping("/api/v1/associate")
public class AssociateController {

    @Autowired
    private ApAssociateService apAssociateService;


    /**
     * 查询搜索的联想词
     * @param dto
     * @return
     */
    @PostMapping("/search")
    public ResponseResult AssociateSearch(@RequestBody UserSearchDto dto){
        return apAssociateService.search(dto);
    }
}
