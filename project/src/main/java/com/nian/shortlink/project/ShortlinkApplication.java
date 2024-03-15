package com.nian.shortlink.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nian.shortlink.project.mapper")
public class ShortlinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkApplication.class, args);
    }
}
