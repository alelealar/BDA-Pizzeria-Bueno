package Negocio.DTOs;

import java.util.List;

public class PedidoDetalleDTO {

    private int idPedido;
    private String folio;
    private String nombreCliente;
    private double total;
    private List<DetallePedidoDTO> detalles;

    public PedidoDetalleDTO() {
    }

    public PedidoDetalleDTO(int idPedido, String folio, String nombreCliente, 
                            double total, List<DetallePedidoDTO> detalles) {
        this.idPedido = idPedido;
        this.folio = folio;
        this.nombreCliente = nombreCliente;
        this.total = total;
        this.detalles = detalles;
    }

    // Getters y Setters

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

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<DetallePedidoDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoDTO> detalles) {
        this.detalles = detalles;
    }
}