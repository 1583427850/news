package xyz.linyh.springbootkafka.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.linyh.springbootkafka.entity.User;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    //用来发送消息
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @GetMapping("/send")
    public String send(){
//        发送消息
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send("topic-second", "key-002", "value-oo2");
        return "ok";
    }

    @GetMapping("/sendobject")
    public User sendObject(){
        User user = new User();
        user.setUsername("xiaozhang");
        user.setPassword("xiaohong");
        kafkaTemplate.send("topic-user",JSON.toJSONString(user));
        return user;
    }
}
