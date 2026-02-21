/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

import java.util.List;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PizzaDTO {
    private String nombre;
    private String tamanio;
    private String descripcion;
    private double precio;
    private String rutaImagen;
    public enum EstadoPizza {
        DISPONIBLE,
        NO_DISPONIBLE
    }

    private EstadoPizza estado;

    public PizzaDTO(String nombre, String tamanio, String descripcion, double precio, String rutaImagen, EstadoPizza estado) {
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
        this.estado = estado;
    }

    

    public PizzaDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public EstadoPizza getEstado() {
        return estado;
    }

    public void setEstado(EstadoPizza estado) {
        this.estado = estado;
    }
    
    

    
}
