package xyz.linyh.webmedia.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SaveIdInterceptor implements HandlerInterceptor {

    public final  static ThreadLocal<Long> WM_USER_THREAD = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        将header里面的id保存到线程中
        String userId = request.getHeader("userId");
        if(userId!=null)
        WM_USER_THREAD.set(Long.valueOf(userId));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        将线程里面的userId删除
        WM_USER_THREAD.remove();
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
