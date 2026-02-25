package Negocio.DTOs;

import java.time.LocalDateTime;

public class PedidoDTO {

    private int idPedido;
    private String nota;
    private String estadoActual;
    private LocalDateTime fechaHoraPedido;  
    private LocalDateTime fechaHoraEntrega;  
    private double total;                    

    public enum Tipo {
        PROGRAMADO, EXPRESS
    }

    private Tipo tipo;

    public PedidoDTO() {
    }

    public PedidoDTO(int idPedido, String nota, String estadoActual,
                     LocalDateTime fechaHoraPedido,
                     LocalDateTime fechaHoraEntrega,
                     Tipo tipo,
                     double total) {

        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.tipo = tipo;
        this.total = total;
    }

    // GETTERS Y SETTERS

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

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}