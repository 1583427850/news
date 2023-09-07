package xyz.linyh.mongotest.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Document("ap_associate_worlds")
public class ApAssociateWorld {

    @MongoId
    private String id;

//    联想词内容
    private String associateWords;

//    创建时间
    private Date createdTime;
}
