package com.elhg.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;


@Component
// IMPORTANTE EL NOMBRE : Ejemplo,  SUFIJO : GatewayFilterFactory
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);

	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
	}

	@Override
	public GatewayFilter apply(Configuracion config) {
		//PRIMER ARGUMENTO EXPRESION LAMBDA
		//SEGUNDO ARGUMENTO ORDEN DEL FLTRO 2
		return (exchange, chain)->{
			//Prefilter
			logger.info("EjemploGatewayFilterFactory.  Ejecutando pre gateway filter factory : ", config.mensaje.toString());
			
			
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				//PostFilter
				//Si esta presente la cookie en el request, lo agrega en el response
				Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
					logger.info("EjemploGatewayFilterFactory.  Agregando cookies en el response: ", config.cookieValor);
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, config.cookieValor).build());
				});
				
				logger.info("EjemploGatewayFilterFactory.  Ejecutando post gateway filter factory : ", config.mensaje);
			}));  
		};
	}
	

	/*
	  Configuracion de nombre de filtro
	 */
	@Override
	public String name() {
		return "EjemploCookie";
	}

	/*
	 En el application.yml 
	 	EjemploCookie=Hola otro mensaje personalizado, usuario, ElielHerrera
	 	Se configura el orden de los parametros
	 */	
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje", "cookieNombre", "cookieValor");
	}

	public static class Configuracion {
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}

	}

}
