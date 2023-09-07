package xyz.linyh.model.webmedia.dto;

import lombok.Data;
import xyz.linyh.model.common.dtos.PageRequestDto;

@Data
public class WmMaterialDto extends PageRequestDto {

    //是否查看收藏的素材0为否，1为是
    private int isCollection;
}
