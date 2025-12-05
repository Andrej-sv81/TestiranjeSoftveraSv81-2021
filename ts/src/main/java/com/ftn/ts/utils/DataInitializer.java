package com.ftn.ts.utils;

import com.ftn.ts.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EventService service;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    @Override
    public void run(String... args) throws Exception {
        if(ddl.equals("create")){
            service.addEventType("Wedding");
            service.addEventType("Birthday");
            service.addEventType("Conference");
            service.addEventType("TeamBuilding");
            logger.info("✅ EventTypes initialized successfully.");
        }else{
            logger.info("✅ Hibernate ddl auto = update");
        }

    }
}