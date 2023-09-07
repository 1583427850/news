package xyz.linyh.essearch.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document("ap_user_search_history")
public class ApUserSearchDto {

//    主键
    @MongoId
    private String id;

//    用户id
    private Long userId;

//    搜索词
    private String keyword;

//    创建时间
    private Date createdTime;
}
