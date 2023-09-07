package xyz.linyh.schedule.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import xyz.linyh.schedule.ScheduleApplication;
import xyz.linyh.schedule.service.CacheService;

@SpringBootTest(classes = ScheduleApplication.class)
public class MyTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void test1(){
        cacheService.zAdd("zset7","im value1",990);

    }
}
