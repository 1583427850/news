package xyz.linyh.model.webmedia.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import xyz.linyh.model.common.dtos.PageRequestDto;

import java.util.Date;

@Data
public class WmNewsListDto extends PageRequestDto {

    @ApiModelProperty("开始时间")
    private Date beginPubDate;

    @ApiModelProperty("结束时间")
    private Date endPubDate;

    @ApiModelProperty("文章频道id")
    private Long channelId;

    @ApiModelProperty("文章关键词")
    private String keyword;

    /**
     *
     *  0 草稿
     *  1 提交（待审核）
     *  2 审核失败
     *  3 人工审核
     *  4 人工审核通过
     *  8 审核通过（待发布）
     *  9 已发布
     */
    @ApiModelProperty("文章状态")
    private Integer status;
}
