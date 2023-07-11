package xyz.linyh.article;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("xyz.linyh.article.mapper")
public class ApArticleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApArticleApplication.class,args);
    }
}
