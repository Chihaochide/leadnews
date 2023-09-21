package com.liuxuanhe.app.gateway.filter;

import com.liuxuanhe.model.user.pojos.ApUser;
import com.liuxuanhe.utils.common.JwtUtils;
import com.liuxuanhe.utils.common.Payload;
import com.liuxuanhe.utils.common.RsaUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

/**
    过滤器复习
 */
@Component
@Order(0)
public class AuthorizeFilter2 implements GlobalFilter{

    @Value("${leadnews.jwt.publicKeyPath}")
    private String publicKeyPath;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (path.contains("/login")){
            System.out.println("该请求是登录请求，直接放行");
            return chain.filter(exchange);
        }
        // 不是登录请求的时候 需要验证token过没过期了
        String token = request.getHeaders().getFirst("token");
        if (token==null){
            // 如果没有携带token，代表不合法访问请求  直接拒绝
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 接下来验证token是否合法
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
            Payload<ApUser> info = JwtUtils.getInfoFromToken(token, publicKey, ApUser.class);
            ApUser apUser = info.getInfo();
            // 将apUser的用户id放入请求头中，方便后续使用
            request.mutate().header("UserId",apUser.getId()+"");
            // 放行下一个过滤器
            return chain.filter(exchange);
        } catch (Exception e) {
            e.printStackTrace();
            // 为空代表不合法，返回401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 终止请求
            return response.setComplete();
        }
    }
}
