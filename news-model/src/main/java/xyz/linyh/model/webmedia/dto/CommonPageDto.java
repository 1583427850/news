package xyz.linyh.model.webmedia.dto;

import lombok.Data;
import xyz.linyh.model.common.dtos.PageRequestDto;

@Data
public class CommonPageDto extends PageRequestDto {

    //频道名称
    private String name;
}
