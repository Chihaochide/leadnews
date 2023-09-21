package com.liuxuanhe.wemedia.gateway.filter;

import com.liuxuanhe.model.wemedia.pojos.WmUser;
import com.liuxuanhe.utils.common.JwtUtils;
import com.liuxuanhe.utils.common.Payload;
import com.liuxuanhe.utils.common.RsaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

@Component
public class AuthorizeFilter implements GlobalFilter {

    @Value("${leadnews.jwt.publicKeyPath}")
    private String publicKeyPath;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (path.contains("/login")){
            System.out.println("该请求是登录请求，放行！");
            return chain.filter(exchange);
        }
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
            Payload<WmUser> payLoad = JwtUtils.getInfoFromToken(token, publicKey, WmUser.class);
            WmUser wmUser = payLoad.getInfo();
            request.mutate().header("userId",wmUser.getId()+"");

            return chain.filter(exchange);
        }catch (Exception e){
            e.printStackTrace();
            // 为空代表不合法，返回401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 终止请求
            return response.setComplete();

        }

    }
}
