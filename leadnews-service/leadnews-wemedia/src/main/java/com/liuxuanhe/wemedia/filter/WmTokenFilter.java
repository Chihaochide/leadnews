package com.liuxuanhe.wemedia.filter;

import com.liuxuanhe.model.wemedia.pojos.WmUser;
import com.liuxuanhe.utils.common.ThreadLocalUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自媒体用户信息过滤器
 * 参数一：过滤器名字（随便写）
 * 参数二：过滤器拦截啊的路径（/*代表全部拦截）
 */
@Component
@WebFilter(filterName = "wmTokenFilter",urlPatterns = "/*")
public class WmTokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 获取请求头信息
        String userId = request.getHeader("userId");
        if (StringUtils.isNoneEmpty(userId)){
            // 存入ThreadLocal
            WmUser wmUser = new WmUser();
            wmUser.setId(Integer.valueOf(userId));
            ThreadLocalUtils.set(wmUser);
        }

        try {
            // 放行
            filterChain.doFilter(request,response); // 执行Controller方法
        } finally {
            // 清空ThreadLocal
            ThreadLocalUtils.remove();
        }


    }
}
