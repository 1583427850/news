package xyz.linyh.model.behavior.dto;

import lombok.Data;
import xyz.linyh.model.annotation.IdEncrypt;

@Data
public class BehaviorLikeDto {

    //文章id
    @IdEncrypt
    private Long articleId;

    //是否点赞
    private Integer operation;

    //类型，0为文章
    private Integer type;
}
