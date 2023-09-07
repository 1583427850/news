package xyz.linyh.model.webmedia.dto;

import lombok.Data;

@Data
public class WmUpOrDownDto {
//    wmnews的id
    private Long id;
//    上架还是下架 0为下架 1为上架
    private Short enable;
}
