package com.ftn.ts.utils;

import com.ftn.ts.model.Admin;
import com.ftn.ts.repository.AdminRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Component
public class AdminGenerator {

    private static String passwordFilePath = "src/main/resources/static/initialSetup.txt";
    private static String adminMail = "testing.app.message@gmail.com";
    private static String name = "Andrej";
    private static int passwordLength = 25;

    private static final Logger logger = LoggerFactory.getLogger(AdminGenerator.class);

    private static String generatePassword(int length) throws IOException {
        if (length < 20) length = 20;
        var password = RandomStringUtils.randomAlphanumeric(length);

        FileWriter passwordFile = new FileWriter(passwordFilePath);
        passwordFile.write(password);
        passwordFile.close();
        return password;
    }

    public static void generateAdmin(AdminRepository repository) throws IOException {
        if (!repository.findAll().isEmpty()) return;
        var admin = new Admin();

        admin.setEmail(adminMail);
        admin.setPassword(generatePassword(passwordLength));
        admin.setName(name);
        admin.setAddress("");
        admin.setPhone("");
        repository.save(admin);
        logger.info("✅ Admin account is generated.");
    }

    public static String getPasswordFromFile() throws FileNotFoundException {
        File file = new File(passwordFilePath);
        Scanner sc = new Scanner(file);
        return sc.nextLine();
    }
}
