package com.bread.traveler.interceptor;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.bread.traveler.constants.Constant;
import com.bread.traveler.context.TripPermissionContext;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    
    @Value("${jwt.secret}")
    private String secret;
    private byte[] JWT_KEY;

    @PostConstruct
    public void init() {
        JWT_KEY = secret.getBytes();
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果是 OPTIONS 请求，直接放行，不校验 Token
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 1. 获取 Header 中的 Token
        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("未登录. URI {}", requestURI);
            return false;
        }

        // 2. 校验 Token 签名是否合法
        boolean verify = false;
        try {
            verify = JWTUtil.verify(token, JWT_KEY);
        } catch (Exception ignored) {}

        if (!verify) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Token 签名验证失败. URI {}", requestURI);
            return false;
        }

        // 3. 解析 userId
        JWT jwt = JWTUtil.parseToken(token);
        String userId = jwt.getPayload("userId").toString();
        String username = jwt.getPayload("username").toString();

        // 4. 校验 Redis 中是否存在 (是否过期或被强制下线)
        String redisToken = redisTemplate.opsForValue().get(Constant.USERS_LOGIN_REDIS_KEY + userId);
        if (redisToken == null || !redisToken.equals(token)) {
            // Token 失效
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Token 已失效. URI {}", requestURI);
            return false;
        }

        // 5. 将 userId 和 username 放入 Request 域，方便 Controller 使用
        request.setAttribute("userId", UUID.fromString(userId));
        request.setAttribute("username", username);
        log.info("用户 {} 登录成功. URI {}", userId, requestURI);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求处理完毕（无论成功失败），清理当前线程的权限缓存
        log.info("Clear Context...");
        TripPermissionContext.clear();
    }
}