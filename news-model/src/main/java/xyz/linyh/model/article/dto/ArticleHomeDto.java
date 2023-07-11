package xyz.linyh.model.article.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 最大时间
    Date maxBehotTime;
    // 最小时间
    Date minBehotTime;
    // 分页size
    Integer size;
    // 频道ID tag如果为_all_ 就是查询所有频道的
    String tag;
}
