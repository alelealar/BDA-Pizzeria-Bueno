/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.DTOs;

/**
 *
 * @author Alejandra Leal Armenta, 262719
 */


public class PizzaProductoDTO {
    private int idPizza;
    private String nombre; 
    private String descripcion;
    private String rutaImagen;
    private String tamanio;
    private double precio;
    private PizzaDTO.EstadoPizza estado;

    public PizzaProductoDTO(int idPizza, String nombre, String descripcion, String rutaImagen, String tamanio, double precio, PizzaDTO.EstadoPizza estado) {
        this.idPizza = idPizza;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.tamanio = tamanio;
        this.precio = precio;
        this.estado = estado;
    }

    public PizzaProductoDTO(String nombre, String descripcion, String rutaImagen, String tamanio, double precio, PizzaDTO.EstadoPizza estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.tamanio = tamanio;
        this.precio = precio;
        this.estado = estado;
    }

    
    
    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public PizzaDTO.EstadoPizza getEstado() {
        return estado;
    }

    public void setEstado(PizzaDTO.EstadoPizza estado) {
        this.estado = estado;
    }
    
    
}
