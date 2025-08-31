package com.ftn.ts;

import com.ftn.ts.repository.AdminRepository;
import com.ftn.ts.utils.AdminGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class TsApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext context = SpringApplication.run(TsApplication.class, args);
        AdminGenerator.generateAdmin(context.getBean(AdminRepository.class));
	}
}
