package com.hoovejd.phototx;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PhototxApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PhototxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.debug("running!");
	}

}