package xyz.linyh.model.webmedia.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class WmChannelSaveDto {

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


    /**
     * 默认排序
     */
    private Integer ord;


}
