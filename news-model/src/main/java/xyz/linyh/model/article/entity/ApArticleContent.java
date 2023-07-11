package xyz.linyh.model.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * APP已发布文章内容表
 * @TableName ap_article_content
 */
@TableName(value ="ap_article_content")
@Data
public class ApArticleContent implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 文章内容
     */
    private String content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}