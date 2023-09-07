package xyz.linyh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.linyh.feign.FeignFallBack.IScheduledClientFallBack;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.schedule.eitity.Taskinfo;

@FeignClient(value = "news-schedule",fallback = IScheduledClientFallBack.class)
public interface IScheduledClient {

//    添加延迟任务
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Taskinfo taskinfo);

    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId);
    @GetMapping("/api/v1/task/{priority}/{type}")
    public ResponseResult pollTask(@PathVariable("priority") int priority,
                                     @PathVariable("type") int type);

////    更新任务状态
//    @GetMapping("/api/v1/update_task_status")
//    public ResponseResult updateTaskStatus();
}
