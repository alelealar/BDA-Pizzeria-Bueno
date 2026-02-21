/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.dominio;

import java.time.LocalDateTime;

/**
 *
 * @author RAYMUNDO
 */
public class Carrito {
    private int idCarrito;
    private int idUsuario;
    private LocalDateTime fechaCreacion;
    private boolean activo;

    public Carrito() {
    }

    public Carrito(int idCarrito, int idUsuario, LocalDateTime fechaCreacion, boolean activo) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Carrito{" + "idCarrito=" + idCarrito + ", idUsuario=" + idUsuario + ", fechaCreacion=" + fechaCreacion + ", activo=" + activo + '}';
    }
}