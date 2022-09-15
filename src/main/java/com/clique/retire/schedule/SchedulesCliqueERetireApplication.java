package com.clique.retire.schedule;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class SchedulesCliqueERetireApplication {

	@PostConstruct
	private void initTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
		System.out.println("Spring boot application running in UTC timezone :" + new Date());
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SchedulesCliqueERetireApplication.class, args);
	}
}