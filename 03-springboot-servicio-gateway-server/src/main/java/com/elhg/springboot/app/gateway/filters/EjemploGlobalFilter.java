package com.elhg.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter,Ordered {

	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("Ejecutando el pre-filter");
		exchange.getRequest().mutate().headers(h-> h.add("token", "123456"));
		
		
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			logger.info("Ejecutando el post-filter");	
			
			//Si esta presente el header en el request, lo agrega en el response
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor ->{
				exchange.getResponse().getHeaders().add("token", valor);
			});
			
			//Agregar una cookie a la respuesta
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			//Agregar un Header, la repuesta será del tipo Text, ya no JSON
			exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		// Con -1 se rompe, 10 o 100, sino se ejecuta antes de otros interceptores que son requisitos.
		return 10;
	}

}
