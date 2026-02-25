/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PedidoProgramadoDTO {

    private int idPedido;
    private String nota;
    private String estadoActual;
    private LocalDateTime fechaHoraPedido;
    private LocalDateTime fechaHoraEntrega;
    
    private int numPedido;
    private int idCliente;
    private String nombreCliente;
    private String idCupon;
    private double porcentajeDescuento;
    private double total;

    public PedidoProgramadoDTO(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, int numPedido, int idCliente, String nombreCliente, String idCupon, double porcentajeDescuento, double total) {
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.numPedido = numPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.idCupon = idCupon;
        this.porcentajeDescuento = porcentajeDescuento;
        this.total = total;
    }

    public PedidoProgramadoDTO(String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, int numPedido, int idCliente, String nombreCliente, String idCupon, double porcentajeDescuento) {
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.numPedido = numPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.idCupon = idCupon;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public PedidoProgramadoDTO(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, int numPedido, int idCliente, String nombreCliente) {
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.numPedido = numPedido;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
    }

    public PedidoProgramadoDTO() {
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public LocalDateTime getFechaHoraPedido() {
        return fechaHoraPedido;
    }

    public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }

    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public int getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(int numPedido) {
        this.numPedido = numPedido;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(String idCupon) {
        this.idCupon = idCupon;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}