package persistencia.daos;

import java.util.List;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link PedidoProgramado}.
 * 
 * Esta interfaz establece el contrato que deben cumplir las clases encargadas de gestionar
 * los pedidos programados en la base de datos.
 * 
 * Los pedidos programados permiten registrar pedidos que serán preparados o entregados
 * en una fecha y hora específica, distinta al momento de su creación.
 * 
 * Incluye operaciones para registrar pedidos, consultar pedidos programados,
 * obtener pedidos por cliente o teléfono, calcular el total del pedido e insertar
 * los detalles asociados al mismo.
 * 
 * Forma parte del patrón DAO (Data Access Object), permitiendo desacoplar la lógica
 * de negocio de la lógica de acceso a datos.
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPedidoProgramadoDAO { 
    
    /**
     * Inserta un nuevo pedido programado en la base de datos.
     * 
     * Este método registra la información general del pedido programado
     * y genera automáticamente su identificador único.
     *
     * @param pp objeto {@link PedidoProgramado} con la información del pedido
     * @return objeto {@link PedidoProgramado} con el identificador generado
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    PedidoProgramado agregarPedidoProgramado(PedidoProgramado pp) throws PersistenciaException;
    
    /**
     * Obtiene todos los pedidos programados registrados en la base de datos.
     *
     * @return lista de objetos {@link PedidoProgramado}
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    List<PedidoProgramado> obtenerPedidosProgramados() throws PersistenciaException;
    
    /**
     * Obtiene todos los pedidos programados asociados a un cliente específico.
     *
     * @param idCliente identificador del cliente
     * @return lista de objetos {@link PedidoProgramado} asociados al cliente
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    List<PedidoProgramado> obtenerPedidosPorCliente(int idCliente) throws PersistenciaException;
    
    /**
     * Obtiene todos los pedidos programados asociados a un número de teléfono.
     *
     * @param telefono número de teléfono del cliente
     * @return lista de objetos {@link PedidoProgramado} asociados al teléfono
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    List<PedidoProgramado> obtenerPedidosPorTelefono(String telefono) throws PersistenciaException;
    
    /**
     * Calcula el total monetario de un pedido programado.
     * 
     * Este cálculo considera todos los productos asociados al pedido y sus cantidades.
     *
     * @param idPedido identificador del pedido programado
     * @return total del pedido programado
     * @throws PersistenciaException si ocurre un error durante el cálculo
     */
    public double calcularTotalProgramado(int idPedido) throws PersistenciaException;
    
    /**
     * Inserta el detalle de un pedido programado en la base de datos.
     * 
     * Este método registra los productos asociados al pedido programado,
     * incluyendo la pizza, cantidad y cualquier nota adicional.
     *
     * @param idPedido identificador del pedido programado
     * @param idPizza identificador de la pizza
     * @param cantidad cantidad solicitada
     * @param nota nota adicional del pedido
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    public void insertarDetalle(int idPedido, int idPizza, int cantidad, String nota) throws PersistenciaException;
    
}