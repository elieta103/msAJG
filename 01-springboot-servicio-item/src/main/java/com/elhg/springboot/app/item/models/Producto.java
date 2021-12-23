package com.elhg.springboot.app.item.models;


public class Producto {

	private Long id;	
	private String nombre;
	private Double precio;
	private java.util.Date createAt;
	private Integer port;
	
	
	
	
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public java.util.Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(java.util.Date createAt) {
		this.createAt = createAt;
	}
	
	
	
}
