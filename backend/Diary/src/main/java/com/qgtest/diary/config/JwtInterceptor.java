package com.qgtest.diary.config;

import com.qgtest.diary.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Locked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtils jwtUtils;
    //响应格式
    private static final String REACT = "application/json;charset=UTF-8";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)
    throws Exception{
        String token = request.getHeader("Authorization");
        if(token == null||token.isEmpty()){
            response.setStatus(401);
            response.setContentType(REACT);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录\"}");//这里要用反斜杠转义“
            return false;
        }
        if(token.startsWith("Bearer ")){
            token = token.substring(7);
        }
        if(!jwtUtils.validateToken(token)){
            response.setStatus(401);
            response.setContentType(REACT);
            response.getWriter().write("{\"code\":401,\"message\":\"token无效或token已过期\"}");//这里要用反斜杠转义“
            return false;
        }//其实这里好像并没有必要，下面get不过的话全局异常处理会捕获异常
        long userId = jwtUtils.getIdByToken(token);
        request.setAttribute("userId",userId);
        return true;
    }
}
