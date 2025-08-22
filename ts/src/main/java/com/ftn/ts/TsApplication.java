package com.ftn.ts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TsApplication.class, args);
	}
}
