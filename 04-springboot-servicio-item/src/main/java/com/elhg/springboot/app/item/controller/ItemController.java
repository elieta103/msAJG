package com.elhg.springboot.app.item.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elhg.springboot.app.item.models.Item;
import com.elhg.springboot.app.item.models.Producto;
import com.elhg.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;



@RestController
public class ItemController {
	
	
	@Autowired
	private CircuitBreakerFactory<?, ?> cbFactory;
	
	@Autowired
	@Qualifier("serviceFeign")  //Feing con balanceador de cargas Ribbon
	//@Qualifier("restTemplate")  //RestTemplate con balanceador de cargas Ribbon
	private ItemService itemService;

	@GetMapping("/listar")
	// Se agregan parametros en el filter 
	public List<Item> listar(@RequestParam(name="nombre", required=false) String nombre,
							 @RequestHeader(name="token-request", required=false) String token){
		System.out.println("Parametros que se anexan en el filter factory son Opcionales:");
		System.out.println(nombre+" - "+token);
		return itemService.findAll();
	}
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad){
		return cbFactory.create("items")
				.run(()->itemService.findById(id, cantidad), (e)->metodoAlternativo(id, cantidad, e));
	}

	// Usando Anotaciones, utiliza el properties
	@CircuitBreaker(name="items", fallbackMethod = "metodoAlternativo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id, @PathVariable Integer cantidad){
		return itemService.findById(id, cantidad);
	}

	// Usando Anotaciones
	//Envuelve la respuesta en una llamda futura asincrona
	//Para que funcione el metodo alterno en ambos casos, solo es necesario agregarlo en Circuit Breaker
	//Aplicaria el metodoAlternativo2 en los 2 casos TimeLimiter y CircuitBreake
	@TimeLimiter(name="items")
	@CircuitBreaker(name="items", fallbackMethod = "metodoAlternativo2")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id, @PathVariable Integer cantidad){
		return CompletableFuture.supplyAsync(() -> itemService.findById(id, cantidad));
	}

	
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e){
		System.out.println("Ocurrio un error : "+ e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony. Metodo Alternativo llamado. Circuit Breaker");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		
		return item;
	}
	
	public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e){
		System.out.println("Ocurrio un error : "+ e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("Camara Sony. Metodo Alternativo llamado. Circuit Breaker");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		
		return CompletableFuture.supplyAsync(() ->item);
	}
	
}
