package com.engineering.littlepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.engineering.littlepay.beans.MainBean;

@SpringBootApplication
public class LittlepayApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(LittlepayApplication.class, args);
		
		MainBean mainRun = context.getBean(MainBean.class);
		
		mainRun.main(args);
	}

}
