package xyz.linyh.model.webmedia.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("新增文章实体类")
public class WmNewsDto {
    @ApiModelProperty("媒体文章id")
    private Long id;

    @ApiModelProperty("频道id")
    private Long channelId;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章内容")
    private String content;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    /**
     * -1 自动
     * 0 无图
     * 1 单图
     * 3 多图
     */
    @ApiModelProperty("文章封面图片类型")
    private Integer type;

    @ApiModelProperty("文章状态（0为草稿 1为提交")
    private Integer status;

    @ApiModelProperty("文章标签")
    private String labels;

    @ApiModelProperty("文章封面图片")
    private List<String> images;



}
