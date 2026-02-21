/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

/**
 *
 * @author RAYMUNDO
 */
public class DetalleCarritoDTO {

    private int idPizza;
    private String nombrePizza;
    private String tamanio;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private String nota;

    public DetalleCarritoDTO(int idPizza, String nombrePizza, String tamanio, int cantidad, double precioUnitario, String nota) {
        this.idPizza = idPizza;
        this.nombrePizza = nombrePizza;
        this.tamanio = tamanio;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.nota = nota;
        this.subtotal = precioUnitario * cantidad;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public double getSubtotal() {
        return subtotal;
    }

    // getters
    public String getNombrePizza() {
        return nombrePizza;
    }

    public void setNombrePizza(String nombrePizza) {
        this.nombrePizza = nombrePizza;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}