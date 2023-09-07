package xyz.linyh.model.user.dto;

import lombok.Data;

@Data
public class UserFollowDto {

    private Long authorId;

    private Long articleId;

    /**
     * 0 关注
     * 1 取消关注
     */
    private Integer operation;
}
