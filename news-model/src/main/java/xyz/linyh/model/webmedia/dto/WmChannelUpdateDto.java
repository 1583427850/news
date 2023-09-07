package xyz.linyh.model.webmedia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class WmChannelUpdateDto {

    /**
     *
     */
    private Long id;

    /**
     * 频道名称
     */
    private String name;

    /**
     * 频道描述
     */
    private String description;


    /**
     *
     */
    private Boolean status;

    private Date createdTime;

    private Boolean isDefault;


    /**
     * 默认排序
     */
    private Integer ord;
}
