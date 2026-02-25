/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PizzaDTO {

    private int idPizza;
    private String nombre;
    private String descripcion;
    private String rutaImagen;

    public enum EstadoPizza {
        DISPONIBLE,
        NO_DISPONIBLE
    }

    private List<String> tamanios = new ArrayList<>();
    private List<Double> precios = new ArrayList<>();
    private EstadoPizza estado;
    //Es la forma mas sencilla que se me ocurrio de almacenar los ids por tamaño de pizza.
    //Basicamente la llave es el tamaño y el valor es el id, se hace de esta manera ya que un tipo de pizza contiene 3 diferentes tamaños y precios
    private Map<String, Integer> idsPorTamanio = new LinkedHashMap<>();

    public PizzaDTO(int idPizza, String nombre, String descripcion, String rutaImagen, EstadoPizza estado) {
        this.idPizza = idPizza;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.estado = estado;
    }

    public void setVariante(String tamanio, double precio, int idPizza) {
        this.tamanios.add(tamanio);
        this.precios.add(precio);
        this.idsPorTamanio.put(tamanio, idPizza);
    }

    public PizzaDTO() {
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

    public Map<String, Integer> getIdsPorTamanio() {
        return idsPorTamanio;
    }

    @Override
    public String toString() {
        return "PizzaDTO{" + "idPizza=" + idPizza + ", nombre=" + nombre + ", descripcion=" + descripcion + ", rutaImagen=" + rutaImagen + ", tamanios=" + tamanios + ", precios=" + precios + ", estado=" + estado + '}';
    }

}
