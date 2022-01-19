package com.elhg.springboot.app.item.clients;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
	
	@Bean("clienteRest")
	@LoadBalanced   //Utilizará Ribbon para el balanceo de cargas con RestTemplate
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
	

}
