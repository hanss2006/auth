package com.hanss.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		//System.setProperty("server.servlet.context-path","/api/auth");
		SpringApplication.run(AuthApplication.class, args);
	}

}
