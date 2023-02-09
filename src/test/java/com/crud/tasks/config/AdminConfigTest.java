package com.crud.tasks.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdminConfigTest {

    @Autowired
    private AdminConfig adminConfig;

    @Value("${admin.mail}")
    private String expectedAdminMail;

    @Test
    void testAdminConfig() {
        assertEquals(expectedAdminMail, "barteklorenowicz258@gmail.com");
    }

}