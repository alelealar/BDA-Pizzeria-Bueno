/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

/**
 *
 * @author RAYMUNDO
 */
public class DetalleCarrito {
    private int idDetalleCarrito;
    private int idCarrito;
    private int idPizza;
    private int cantidad;
    private String nota;
    private String tamanio;

    public DetalleCarrito() {
    }

    public DetalleCarrito(int idDetalleCarrito, int idCarrito, int idPizza, int cantidad, String nota) {
        this.idDetalleCarrito = idDetalleCarrito;
        this.idCarrito = idCarrito;
        this.idPizza = idPizza;
        this.cantidad = cantidad;
        this.nota = nota;
    }

    public int getIdDetalleCarrito() {
        return idDetalleCarrito;
    }

    public void setIdDetalleCarrito(int idDetalleCarrito) {
        this.idDetalleCarrito = idDetalleCarrito;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdPizza() {
        return idPizza;
    }

    public void setIdPizza(int idPizza) {
        this.idPizza = idPizza;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getTamanio() {
        return tamanio;
    }

    public void setTamanio(String tamanio) {
        this.tamanio = tamanio;
    }
}