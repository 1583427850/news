package xyz.linyh.webmedia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("xyz.linyh.webmedia.mapper")
@EnableFeignClients(basePackages = "xyz.linyh.feign")//让feign可以扫描到feign微服务下的对应接口
@EnableAsync//开启异步调用
@EnableScheduling //开启任务调度
public class WebMediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMediaApplication.class,args);
    }
}
