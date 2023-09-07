package xyz.linyh.schedule.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import xyz.linyh.feign.IScheduledClient;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.schedule.eitity.Taskinfo;
import xyz.linyh.schedule.service.TaskinfoService;

import java.util.List;

@Component
@RestController
public class ScheduledClient implements IScheduledClient {

    @Autowired
    private TaskinfoService taskinfoService;

    //    添加延迟任务
    @Override
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Taskinfo taskinfo){
        Long taskId = taskinfoService.addTask(taskinfo);
        return ResponseResult.okResult(taskId);
    }

    @Override
    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId){
        boolean isCancel = taskinfoService.cancelTask(taskId);
        return ResponseResult.okResult(isCancel);
    }

    @Override
    @GetMapping("/api/v1/task/{priority}/{type}")
    public ResponseResult<Taskinfo> pollTask(int priority, int type) {
        Taskinfo taskinfo = taskinfoService.poll(priority, type);
        return ResponseResult.okResult(taskinfo);
    }
}
