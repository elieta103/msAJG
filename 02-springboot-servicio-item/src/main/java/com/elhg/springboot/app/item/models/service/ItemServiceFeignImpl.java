package com.elhg.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhg.springboot.app.item.clients.ProductoClienteFeignRest;
import com.elhg.springboot.app.item.models.Item;


@Service("serviceFeign")
public class ItemServiceFeignImpl implements ItemService {

	@Autowired
	private ProductoClienteFeignRest clienteFeignRest;
	
	@Override
	public List<Item> findAll() {
		return clienteFeignRest.listar().stream().map(p -> new Item(p,1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		return new Item(clienteFeignRest.detalle(id), cantidad);
	}

}
