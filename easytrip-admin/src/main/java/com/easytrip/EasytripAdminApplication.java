package com.easytrip;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(scanBasePackages = {"com.easytrip"})
@MapperScan(basePackages = {"com.easytrip.mappers"})
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 2400, redisNamespace = "easytrip:session")
public class EasytripAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasytripAdminApplication.class, args);
    }
}
