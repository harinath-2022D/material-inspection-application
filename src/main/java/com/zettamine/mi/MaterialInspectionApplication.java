package com.zettamine.mi;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MaterialInspectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaterialInspectionApplication.class, args);
//		try {
//			Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler http://localhost:8080/index.html");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
