/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

import java.time.LocalDateTime;

/**
 *
 * @author RAYMUNDO
 */
public class PedidoTablaDTO {
    private int idPedido;
    private String folio;
    private String cliente;
    private String telefono;
    private LocalDateTime fechaHora;
    private double total;
    private String estado;
    private String tipo;

    public PedidoTablaDTO(int idPedido, String folio, String cliente, String telefono, LocalDateTime fechaHora, double total, String estado, String tipo) {
        this.idPedido = idPedido;
        this.folio = folio;
        this.cliente = cliente;
        this.telefono = telefono;
        this.fechaHora = fechaHora;
        this.total = total;
        this.estado = estado;
        this.tipo = tipo;
    }
    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}