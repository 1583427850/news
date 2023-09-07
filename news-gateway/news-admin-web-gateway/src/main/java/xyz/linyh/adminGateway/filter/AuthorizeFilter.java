package xyz.linyh.adminGateway.filter;

import com.alibaba.cloud.commons.lang.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import xyz.linyh.utils.common.AppJwtUtil;

/**
 * 用来网关对admin端用户是否登录进行校验
 */
@Component
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1.获取请求和获取token
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String token = request.getHeaders().getFirst("token");
//        2.判断是否是登录页面
        String url = request.getURI().getPath();
        if(url.contains("login")){
//            执行下一个过滤器
            return chain.filter(exchange);
        }
//        2.判断是否携带token
        if(StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            结束退出过滤器，直接返回
            return response.setComplete();
        }
        Claims claimsBody = null;
//        3.判断token是否有效，如果无效会抛出异常
        try {
            claimsBody = AppJwtUtil.getClaimsBody(token);
            int i = AppJwtUtil.verifyToken(claimsBody);
            if(i==1 || i==2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
    //            结束退出过滤器，直接返回
                return response.setComplete();
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            结束退出过滤器，直接返回
            return response.setComplete();
        }

        //        获取用户id
        Object userId = claimsBody.get("id");
//        将用户id保存到请求头中
        ServerHttpRequest newRequest = request.mutate().header("userId", userId.toString()).build();
//        将exchange里面的request修改为改完的
        exchange.mutate().request(newRequest);

//        验证成功，执行下一个filter
        return chain.filter(exchange);
    }

    /**
     * 用来指定这个filter优先级，越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
