package xyz.linyh.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    NEWS_SCAN_TIME(1001,1,"文章定时审核"),
    REMOTEERROR(1002,2,"第三方接口调用失败，重试");

    private final int taskType;
    private final int priority;
    private final String desc;
}
