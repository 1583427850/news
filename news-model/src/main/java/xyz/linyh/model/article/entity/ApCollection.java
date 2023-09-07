package xyz.linyh.model.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * APP收藏信息表
 * @TableName ap_collection
 */
@TableName(value ="ap_collection")
@Data
public class ApCollection implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 实体ID
     */
    private Object entryId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 点赞内容类型
            0文章
            1动态
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date collectionTime;

    /**
     * 发布时间
     */
    private Date publishedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}