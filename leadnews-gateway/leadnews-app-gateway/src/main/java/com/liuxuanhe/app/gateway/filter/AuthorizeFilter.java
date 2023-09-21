//package com.heima.app.gateway.filter;
//
//import com.heima.utils.common.JwtUtils;
//import com.heima.utils.common.Payload;
//import com.heima.utils.common.RsaUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import user.pojos.ApUser;
//
//import java.security.PublicKey;
//
///**
// * 统一鉴权过滤器
// */
//@Component
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//    @Value("${leadnews.jwt.publicKeyPath}")
//    private String publicKeyPath;
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        // 获取请求和响应
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpResponse response = exchange.getResponse();
//
//        // 获取请求路径
//        String uri = request.getURI().getPath(); // user/api/v1/login/login_auth
//        // 判断该请求是否为登录请求
//        if (uri.contains("/login")){
//            // 直接放行
//            return chain.filter(exchange);
//        }
////        String flag = uri.substring(uri.lastIndexOf("/"));
//        // 获取token请求头
//        String token = request.getHeaders().getFirst("token");
//        if (StringUtils.isEmpty(token)){
//            // 为空代表不合法，返回401
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            // 终止请求
//            return response.setComplete();
//        }
//        // 获取公钥
//        try {
//            PublicKey publicKey = RsaUtils.getPublicKey(publicKeyPath);
//            // 验证Token是否合法
//            Payload<ApUser> payload = JwtUtils.getInfoFromToken(token, publicKey, ApUser.class);
//            // 取出登录用户的id 并存入请求头
//            ApUser user = payload.getInfo();
//            request.mutate().header("userId",user.getId().toString());
//            // 放行
//            return chain.filter(exchange);
//        } catch (Exception e) {
//        }
//    }
//
//    /**
//     * 数值越大，优先级越低
//     * 也就是说 级别越大 越后被调用
//     * @return
//     */
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
