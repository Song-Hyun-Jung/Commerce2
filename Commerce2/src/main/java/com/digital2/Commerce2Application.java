package com.digital2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.digital2.lucene.DataHandler;

@SpringBootApplication
public class Commerce2Application {

	public static void main(String[] args) {
		System.out.println("hello2");
		SpringApplication.run(Commerce2Application.class, args);
		DataHandler d = new DataHandler();
	}

}
