package com.qgtest.diary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/users/register",
                        "/api/users/login"
                );
    }
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保路径以 / 结尾
        String location = uploadPath;
        if (!location.endsWith("/")) {
            location += "/";
        }
        
        // 如果是相对路径，转换为绝对路径
        if (!location.startsWith("/") && !location.matches("^[A-Za-z]:.*")) {
            String userDir = System.getProperty("user.dir");
            location = userDir + java.io.File.separator + "uploads" + java.io.File.separator + "avatars" + java.io.File.separator;
        }
        
        System.out.println("✅ 静态资源映射已配置:");
        System.out.println("   /uploads/avatars/** -> file:" + location);
        System.out.println("   /avatars/** -> file:" + location);
        
        // 映射 /uploads/avatars/** 到上传目录
        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations("file:" + location);
        
        // 同时映射 /avatars/** （兼容前端直接请求 avatars/xxx）
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + location);
    }
}
