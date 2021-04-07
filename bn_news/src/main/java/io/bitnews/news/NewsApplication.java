package io.bitnews.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "io.bitnews" })
@EnableFeignClients(basePackages = { "io.bitnews" })
@EnableEurekaClient
@EnableScheduling
public class NewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class,args);
	}

}
