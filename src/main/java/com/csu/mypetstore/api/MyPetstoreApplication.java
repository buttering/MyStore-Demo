package com.csu.mypetstore.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MyPetstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyPetstoreApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello(){
		return "hello spring!";
	}
}
