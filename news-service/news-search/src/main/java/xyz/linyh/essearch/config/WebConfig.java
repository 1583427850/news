package xyz.linyh.essearch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.linyh.essearch.interceptor.SaveUserIdInterceptor;

/**
 * 新增拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaveUserIdInterceptor())
                .addPathPatterns("/**");

    }
}
