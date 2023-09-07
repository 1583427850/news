package xyz.linyh.behavior.interceptor;


import org.springframework.web.servlet.HandlerInterceptor;
import xyz.linyh.model.user.entity.ApUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetIdInterceptor implements HandlerInterceptor {

    public static ThreadLocal<Long> AP_USER_THREAD = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = request.getHeader("userId");
        AP_USER_THREAD.set(Long.valueOf(userId));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AP_USER_THREAD.remove();
    }
}
