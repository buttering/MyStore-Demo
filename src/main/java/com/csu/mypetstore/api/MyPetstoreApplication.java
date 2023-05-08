package com.csu.mypetstore.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@MapperScan("com.csu.mypetstore.api.persistence")  // 自动扫描mapper，不用再在mapper手动使用@mapper
public class MyPetstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyPetstoreApplication.class, args);
	}

}
