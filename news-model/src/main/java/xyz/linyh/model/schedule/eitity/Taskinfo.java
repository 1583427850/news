package xyz.linyh.model.schedule.eitity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName taskinfo
 */
@TableName(value ="taskinfo")
@Data
public class Taskinfo implements Serializable {
    /**
     * 任务id
     */
    @TableId
    private Long taskId;

    /**
     * 执行时间
     */
    private Date executeTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 参数
     */
    private byte[] parameters;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}