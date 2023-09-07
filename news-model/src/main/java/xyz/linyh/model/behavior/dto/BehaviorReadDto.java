package xyz.linyh.model.behavior.dto;

import lombok.Data;

@Data
public class BehaviorReadDto {

//    阅读的文章
    private Long articleId;

//    阅读次数
    private Long count;
}
