package xyz.linyh.feign.FeignFallBack;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.linyh.feign.IScheduledClient;
import xyz.linyh.model.common.dtos.ResponseResult;
import xyz.linyh.model.schedule.eitity.Taskinfo;

@Component
@Slf4j
public class IScheduledClientFallBack implements IScheduledClient {
    @Override
    public ResponseResult addTask(Taskinfo taskinfo) {
        log.info("到了addTask（）的降级方法。。。。");
        return null;
    }

    @Override
    public ResponseResult cancelTask(long taskId) {
        log.info("到了cancelTask（）的降级方法。。。。");
        return null;
    }

    @Override
    public ResponseResult pollTask(int priority, int type) {
        log.info("pollTask（）的降级方法。。。。");
        return null;
    }
}
