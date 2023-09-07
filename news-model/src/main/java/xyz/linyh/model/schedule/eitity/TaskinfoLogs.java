package xyz.linyh.model.schedule.eitity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName taskinfo_logs
 */
@TableName(value ="taskinfo_logs")
@Data
public class TaskinfoLogs implements Serializable {
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
     * 版本号,用乐观锁
     */
    @Version
    private Integer version;

    /**
     * 状态 0=初始化状态 1=EXECUTED 2=CANCELLED
     */
    private Integer status;

    /**
     * 参数
     */
    private byte[] parameters;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}