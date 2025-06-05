package com.passwordmanager.password_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//This is used to the app don't crash when mongo is not configured
public class PasswordManagerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerBackendApplication.class, args);
	}

}
