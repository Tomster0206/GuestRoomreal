package com.guestroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class GuestRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuestRoomApplication.class, args);
    }

}
