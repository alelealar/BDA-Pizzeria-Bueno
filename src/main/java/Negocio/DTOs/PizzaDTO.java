/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PizzaDTO {

    private int idPizza;
    private String nombre;
    private String tamanio;
    private String descripcion;
    private String rutaImagen;
    private double precio;

    public enum EstadoPizza {
        DISPONIBLE,
        NO_DISPONIBLE
    }
    
    //hay 3 tamaños de pizza, ocupo almacenarlos todos para poder mostrar en la venta los disponibles.
    private List<String> tamanios = new ArrayList<>();
    //cada tamaño de pízza tiene un preecio diferente-
    private List<Double> precios = new ArrayList<>();
    //cada pizza tiene un id eapecifico, ocupo almacenar los de los 3 tamaños.
    private List<Integer> idPizzas = new ArrayList<>();
    private EstadoPizza estado;

    public PizzaDTO(int idPizza, String nombre, String descripcion, String rutaImagen, EstadoPizza estado) {
        this.idPizza = idPizza;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.estado = estado;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setVariante(String tamanio, double precio, int idPizza) {
        this.tamanios.add(tamanio);
        this.precios.add(precio);
        this.idPizzas.add(idPizza);
    }

    public PizzaDTO() {
    }

    public PizzaDTO(int idPizza, String nombre, String tamanio, String descripcion, String rutaImagen, EstadoPizza estado) {
        this.idPizza = idPizza;
        this.nombre = nombre;
        this.tamanio = tamanio;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.estado = estado;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
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

    public EstadoPizza getEstado() {
        return estado;
    }

    public void setEstado(EstadoPizza estado) {
        this.estado = estado;
    }

    public List<String> getTamanios() {
        return tamanios;
    }

    public List<Double> getPrecios() {
        return precios;
    }

    @Override
    public String toString() {
        return "PizzaDTO{" + "idPizza=" + idPizza + ", nombre=" + nombre + ", descripcion=" + descripcion + ", rutaImagen=" + rutaImagen + ", tamanios=" + tamanios + ", precios=" + precios + ", estado=" + estado + '}';
    }

}
