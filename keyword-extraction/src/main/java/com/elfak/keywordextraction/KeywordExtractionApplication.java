package com.elfak.keywordextraction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.elfak")
public class KeywordExtractionApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeywordExtractionApplication.class, args);
	}

}
