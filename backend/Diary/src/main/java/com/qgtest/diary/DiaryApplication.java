package com.qgtest.diary;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.qgtest.diary.mapper")
public class DiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryApplication.class, args);
    }
    @Bean
    CommandLineRunner run() {
        return args -> {
            System.out.println("\n========================================");
            System.out.println("✅ DiaryApplication 启动成功！");
            System.out.println("🌐 访问地址: http://localhost:8801");
            System.out.println("📚 API文档: http://localhost:8801/swagger-ui.html");
            System.out.println("========================================\n");
        };
    }
}
