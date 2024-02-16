package com.zettamine.mi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaterialInspectionApplication {
	
	private static Logger LOG = LoggerFactory.getLogger(MaterialInspectionApplication.class);

	public static void main(String[] args) {
		
		LOG.info("Loading main class");
		
		SpringApplication.run(MaterialInspectionApplication.class, args);

	}

}
