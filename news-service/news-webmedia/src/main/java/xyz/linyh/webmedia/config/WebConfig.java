package xyz.linyh.webmedia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.linyh.webmedia.interceptor.SaveIdInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SaveIdInterceptor saveIdInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(saveIdInterceptor).addPathPatterns("/**");
    }
}
