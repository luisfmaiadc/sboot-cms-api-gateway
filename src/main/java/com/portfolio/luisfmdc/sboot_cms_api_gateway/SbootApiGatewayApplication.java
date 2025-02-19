package com.portfolio.luisfmdc.sboot_cms_api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SbootApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbootApiGatewayApplication.class, args);
	}

}
