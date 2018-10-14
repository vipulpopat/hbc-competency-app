package com.hbc.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColorApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(ColorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ColorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String fileName = args[0];
		ColorProcessor colorProcessor = new InputFileParser(fileName).parseFile();
		logger.debug("Color Processor = " + colorProcessor);
		colorProcessor.getBatchCombination();

	}
}