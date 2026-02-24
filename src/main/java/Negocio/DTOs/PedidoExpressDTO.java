package Negocio.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase DTO que representa un PedidoExpress.
 *
 * Se utiliza para transportar informacion entre la capa de presentacion y la
 * capa de negocio.
 *
 * @author Brian
 */
public class PedidoExpressDTO {

    private int idPedido;
    private String nota;
    private String estadoActual;
    private LocalDateTime fechaHoraPedido;
    private LocalDateTime fechaHoraEntrega;

    private String PIN;
    private String folio;

    /**
     * Constructor vacio.
     */
    public PedidoExpressDTO() {
    }

    /**
     * Constructor con todos los atributos.
     *
     * @param idPedido Identificador del pedido.
     * @param nota Nota adicional del pedido.
     * @param estadoActual Estado actual del pedido.
     * @param fechaHoraPedido Fecha y hora en que se realizo el pedido.
     * @param fechaHoraEntrega Fecha y hora estimada de entrega.
     * @param PIN Codigo PIN del pedido.
     * @param folio Folio del pedido.
     */
    public PedidoExpressDTO(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, String PIN, String folio) {
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.PIN = PIN;
        this.folio = folio;
    }

    /**
     * Constructor sin id.
     *
     * Se utiliza principalmente cuando el pedido aun no ha sido persistido en
     * base de datos.
     *
     * @param nota Nota adicional del pedido.
     * @param estadoActual Estado actual del pedido.
     * @param fechaHoraPedido Fecha y hora en que se realizo el pedido.
     * @param fechaHoraEntrega Fecha y hora estimada de entrega.
     * @param PIN Codigo PIN del pedido.
     * @param folio Folio del pedido.
     */
    public PedidoExpressDTO(String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, String PIN, String folio) {
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.PIN = PIN;
        this.folio = folio;
    }

    public PedidoExpressDTO(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega) {
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    /**
     * Obtiene el id del pedido.
     *
     * @return idPedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * Asigna el id del pedido.
     *
     * @param idPedido Identificador del pedido.
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene la nota del pedido.
     *
     * @return nota
     */
    public String getNota() {
        return nota;
    }

    /**
     * Asigna la nota del pedido.
     *
     * @param nota Nota del pedido.
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * Obtiene el estado actual del pedido.
     *
     * @return estadoActual
     */
    public String getEstadoActual() {
        return estadoActual;
    }

    /**
     * Asigna el estado actual del pedido.
     *
     * @param estadoActual Estado del pedido.
     */
    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    /**
     * Obtiene la fecha y hora en que se realizo el pedido.
     *
     * @return fechaHoraPedido
     */
    public LocalDateTime getFechaHoraPedido() {
        return fechaHoraPedido;
    }

    /**
     * Asigna la fecha y hora del pedido.
     *
     * @param fechaHoraPedido Fecha y hora del pedido.
     */
    public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }

    /**
     * Obtiene la fecha y hora estimada de entrega.
     *
     * @return fechaHoraEntrega
     */
    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    /**
     * Asigna la fecha y hora de entrega.
     *
     * @param fechaHoraEntrega Fecha y hora de entrega.
     */
    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    /**
     * Obtiene el PIN del pedido.
     *
     * @return PIN
     */
    public String getPIN() {
        return PIN;
    }

    /**
     * Asigna el PIN del pedido.
     *
     * @param PIN Codigo PIN del pedido.
     */
    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    /**
     * Obtiene el folio del pedido.
     *
     * @return folio
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Asigna el folio del pedido.
     *
     * @param folio Folio del pedido.
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

}
