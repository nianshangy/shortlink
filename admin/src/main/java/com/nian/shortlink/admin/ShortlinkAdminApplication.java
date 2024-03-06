package com.nian.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nian.shortlink.admin.mapper")
public class ShortlinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkAdminApplication.class,args);
    }
}
