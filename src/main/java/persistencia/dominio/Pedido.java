package persistencia.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase base que representa un Pedido dentro del sistema.
 *
 * <p>
 * Un pedido contiene información general como el estado actual, fechas de
 * realización y entrega, así como notas adicionales. Esta clase sirve como base
 * para distintos tipos de pedidos, como pedidos express y pedidos
 * programados.</p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class Pedido {

    /**
     * Identificador único del pedido.
     */
    private int idPedido;

    /**
     * Nota adicional del pedido.
     */
    private String nota;

    /**
     * Estado actual del pedido (pendiente, en preparación, entregado, etc.).
     */
    private String estadoActual;

    /**
     * Fecha y hora en que se realizó el pedido.
     */
    private LocalDateTime fechaHoraPedido;

    /**
     * Fecha y hora programada para la entrega del pedido.
     */
    private LocalDateTime fechaHoraEntrega;
    
    public enum Tipo{
        PROGRAMADO, EXPRESS
    }
    private Tipo tipo;

    /**
     * Constructor por omisión.
     */
    public Pedido() {
    }
    
    /**
     * Constructor completo que incluye identificador.
     *
     * @param idPedido identificador del pedido
     * @param nota nota adicional
     * @param estadoActual estado actual del pedido
     * @param fechaHoraPedido fecha y hora del pedido
     * @param fechaHoraEntrega fecha y hora de entrega
     * @param tipo
     */
    public Pedido(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, Tipo tipo) {    
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.tipo = tipo;
    }

    public Pedido(int idPedido, String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega) {
        this.idPedido = idPedido;
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
    }
    
    /**
     * Constructor sin identificador.
     *
     * @param nota nota adicional
     * @param estadoActual estado actual del pedido
     * @param fechaHoraPedido fecha y hora del pedido
     * @param fechaHoraEntrega fecha y hora de entrega
     * @param tipo
     */
    public Pedido(String nota, String estadoActual, LocalDateTime fechaHoraPedido, LocalDateTime fechaHoraEntrega, Tipo tipo) {    
        this.nota = nota;
        this.estadoActual = estadoActual;
        this.fechaHoraPedido = fechaHoraPedido;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.tipo = tipo;
    }
    

    /**
     * @return identificador del pedido
     */
    public int getIdPedido() {
        return idPedido;
    }

    /**
     * @param idPedido nuevo identificador
     */
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * @return nota del pedido
     */
    public String getNota() {
        return nota;
    }

    /**
     * @param nota nueva nota
     */
    public void setNota(String nota) {
        this.nota = nota;
    }

    /**
     * @return estado actual del pedido
     */
    public String getEstadoActual() {
        return estadoActual;
    }

    /**
     * @param estadoActual nuevo estado
     */
    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    /**
     * @return fecha y hora del pedido
     */
    public LocalDateTime getFechaHoraPedido() {
        return fechaHoraPedido;
    }

    /**
     * @param fechaHoraPedido nueva fecha y hora del pedido
     */
    public void setFechaHoraPedido(LocalDateTime fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }

    /**
     * @return fecha y hora de entrega
     */
    public LocalDateTime getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }

    /**
     * @param fechaHoraEntrega nueva fecha y hora de entrega
     */
    public void setFechaHoraEntrega(LocalDateTime fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }


    /**
     * Devuelve una representación en cadena del pedido.
     *
     * @return información textual del pedido
     */
    @Override
    public String toString() {
        return "Pedido{"
                + "idPedido=" + idPedido
                + ", nota=" + nota
                + ", estadoActual=" + estadoActual
                + ", fechaHoraPedido=" + fechaHoraPedido
                + ", fechaHoraEntrega=" + fechaHoraEntrega
                + '}';
    }
}
