/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.DTOs;

import persistencia.dominio.Pedido;


/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */


public class DetallePedidoDTO {
    private int idDetallePedido;
    private double precio;
    private String nota;
    private int cantidad;
    private double subtotal;
    private int idPedido;
    private int idPizza;

    public DetallePedidoDTO(int idDetallePedido, double precio, String nota, int cantidad, double subtotal, int idPedido, int idPizza) {
        this.idDetallePedido = idDetallePedido;
        this.precio = precio;
        this.nota = nota;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.idPedido = idPedido;
        this.idPizza = idPizza;
    }

    public DetallePedidoDTO(double precio, String nota, int cantidad, double subtotal, int idPedido, int idPizza) {
        this.precio = precio;
        this.nota = nota;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.idPedido = idPedido;
        this.idPizza = idPizza;
    }

    public DetallePedidoDTO() {
    }
    
    

    public int getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(int idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }
    
    
    
}
