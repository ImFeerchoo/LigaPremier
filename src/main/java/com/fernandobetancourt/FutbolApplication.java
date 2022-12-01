package com.fernandobetancourt;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FutbolApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FutbolApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(LocalDateTime.now());
	}

}
