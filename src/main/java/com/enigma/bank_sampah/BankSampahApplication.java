package com.enigma.bank_sampah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BankSampahApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankSampahApplication.class, args);
	}

}
