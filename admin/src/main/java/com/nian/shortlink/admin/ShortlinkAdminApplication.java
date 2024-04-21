package com.nian.shortlink.admin;

import com.nian.shortlink.admin.remote.ShortLinkActualRemoteService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(clients = {ShortLinkActualRemoteService.class})
@MapperScan("com.nian.shortlink.admin.mapper")
public class ShortlinkAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShortlinkAdminApplication.class,args);
    }
}