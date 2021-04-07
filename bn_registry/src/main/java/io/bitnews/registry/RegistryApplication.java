package io.bitnews.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication{

	public static void main(String[] args) {
		SpringApplication.run(RegistryApplication.class, args);
	}

}
