package com.easytrip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.easytrip"})
@MapperScan(basePackages = {"com.easytrip.mappers"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
public class EasytripWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasytripWebApplication.class, args);
    }
}
