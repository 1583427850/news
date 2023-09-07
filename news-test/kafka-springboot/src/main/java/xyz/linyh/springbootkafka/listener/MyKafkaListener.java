package xyz.linyh.springbootkafka.listener;

import com.alibaba.fastjson.JSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import xyz.linyh.springbootkafka.entity.User;


//接收kafka消息
@Component
public class MyKafkaListener {

    @KafkaListener(topics = {"topic-second"})
    public void onMessage(String message){
        if(StringUtils.isNotBlank(message)){
            System.out.println(message);
        }
    }

    @KafkaListener(topics = {"topic-user"})
    public void onMessage2(String message){
        if(StringUtils.isNotBlank(message)){
            System.out.println(message);
            System.out.println("----------------");
            System.out.println(JSON.parseObject(message, User.class));
        }
    }
}
