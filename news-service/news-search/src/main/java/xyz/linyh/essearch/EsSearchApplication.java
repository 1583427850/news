package xyz.linyh.essearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync //开启异步调用
public class EsSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsSearchApplication.class,args);
    }
}
