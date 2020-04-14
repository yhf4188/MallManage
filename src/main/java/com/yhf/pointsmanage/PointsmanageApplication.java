package com.yhf.pointsmanage;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PointsmanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointsmanageApplication.class, args);
    }

}
