package io.bitnews.news_bg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = { "io.bitnews" })
@EnableFeignClients(basePackages = { "io.bitnews" })
@EnableScheduling
public class BgNewsApplication {
	public static void main(String[] args) {
		SpringApplication.run(BgNewsApplication.class, args);
	}
}
