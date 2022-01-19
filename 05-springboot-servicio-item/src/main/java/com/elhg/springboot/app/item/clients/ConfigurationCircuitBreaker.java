package com.elhg.springboot.app.item.clients;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class ConfigurationCircuitBreaker {
	
	// Para el manejo del circuitBreaker, Se prueba directamente, sin el apiGateway
	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer(){
		//Configura los valores para cada circuitBreaker, solo hay uno: cbFactory.create("items")
		//En los customizados se invoca el build(), en los default no
		return factory -> factory.configureDefault(id->{				
			return new Resilience4JConfigBuilder(id)
					.circuitBreakerConfig(CircuitBreakerConfig.custom()			//Puede ser default o custom , por default o personalizada
							.slidingWindowSize(10)   		  						//Tamaño de la ventana deslizante, por defecto es 100 Request
							.failureRateThreshold(50)     						//Tasa de fallas el umbral, por defecto es del 50%
							.waitDurationInOpenState(Duration.ofSeconds(10L)) 	//Tiempo de espera en el estado abierto, por default es 1 minuto
							.permittedNumberOfCallsInHalfOpenState(5)			//Numero de llamadas en estado semiabierto, por default son 10
							.slowCallRateThreshold(50)							//Llamadas lentas en porcentaje, default 100%
							.slowCallDurationThreshold(Duration.ofSeconds(2L))				//Tiempo maximo que debiese tardar una llamada, si lo supea es una llamada lenta
							.build())
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build())			//TimeOut  TiempoLimite TimeLimiterConfig.ofDefaults() es 1 seg
					.build();											//TimeOut debe ser mayor que la llamada lenta, 
									
		});
		
	}

}
