package persistencia.daos;

import java.time.LocalDate;
import java.util.List;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoResumen;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link Pedido}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object) y establece el contrato
 * que deben cumplir las clases encargadas de gestionar los pedidos en la base de datos.
 * 
 * Permite realizar operaciones como registrar, actualizar, consultar, validar y cambiar
 * el estado de los pedidos, así como obtener información resumida para visualización.
 * 
 * Su uso permite desacoplar la lógica de acceso a datos de la lógica de negocio,
 * facilitando el mantenimiento, la escalabilidad y las pruebas del sistema.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPedidoDAO {

    /**
     * Inserta un nuevo pedido en la base de datos.
     * 
     * Este método registra un nuevo pedido y genera automáticamente su identificador único.
     * 
     * @param pedido objeto {@link Pedido} que contiene la información del pedido
     * @return objeto {@link Pedido} con el identificador generado
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    public Pedido agregarPedido(Pedido pedido) throws PersistenciaException;

    /**
     * Actualiza la información de un pedido existente.
     * 
     * Permite modificar los datos de un pedido previamente registrado en la base de datos.
     * 
     * @param pedido objeto {@link Pedido} con la información actualizada
     * @return objeto {@link Pedido} actualizado
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    public Pedido actualizarPedido(Pedido pedido) throws PersistenciaException;

    /**
     * Busca un pedido utilizando su identificador único.
     * 
     * @param id identificador del pedido
     * @return objeto {@link Pedido} encontrado
     * @throws PersistenciaException si el pedido no existe o ocurre un error durante la consulta
     */
    public Pedido buscarPedidoPorId(int id) throws PersistenciaException;

    /**
     * Consulta los pedidos dentro de un rango de fechas específico.
     * 
     * @param fechaInicio fecha inicial del rango
     * @param fechaFin fecha final del rango
     * @return lista de objetos {@link Pedido} dentro del rango indicado
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<Pedido> consultarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) throws PersistenciaException;

    /**
     * Obtiene todos los pedidos registrados en la base de datos.
     * 
     * @return lista de objetos {@link Pedido}
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<Pedido> obtenerPedidos() throws PersistenciaException;

    /**
     * Obtiene una lista resumida de los pedidos para su visualización en tablas.
     * 
     * Esta información suele incluir datos relevantes como cliente, fecha, total y estado.
     * 
     * @return lista de objetos {@link PedidoResumen}
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<PedidoResumen> obtenerPedidosTabla() throws PersistenciaException;

    /**
     * Obtiene una lista de pedidos filtrados según un criterio específico.
     * 
     * El filtro puede aplicarse por estado, cliente, fecha u otros criterios definidos.
     * 
     * @param filtro criterio de filtrado
     * @return lista de objetos {@link PedidoResumen} que cumplen el filtro
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<PedidoResumen> obtenerPedidosFiltrados(String filtro) throws PersistenciaException;

    /**
     * Obtiene el detalle completo de un pedido específico.
     * 
     * Incluye toda la información relacionada al pedido, como productos, cantidades y estado.
     * 
     * @param idPedido identificador del pedido
     * @return objeto {@link Pedido} con su información completa
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public Pedido obtenerDetallePedido(int idPedido) throws PersistenciaException;

    /**
     * Valida que el folio y el PIN proporcionados correspondan a un pedido específico.
     * 
     * Este método se utiliza como medida de seguridad para confirmar la identidad del pedido.
     * 
     * @param idPedido identificador del pedido
     * @param folio folio del pedido
     * @param pin PIN de verificación del pedido
     * @return true si el folio y PIN son válidos, false en caso contrario
     * @throws PersistenciaException si ocurre un error durante la validación
     */
    public boolean validarFolioYPIN(int idPedido, String folio, String pin) throws PersistenciaException;

    /**
     * Cambia el estado actual de un pedido.
     * 
     * Este método permite actualizar el estado del pedido, por ejemplo:
     * PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO, etc.
     * 
     * @param idPedido identificador del pedido
     * @param nuevoEstado nuevo estado del pedido
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    public void cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException;

    /**
     * Obtiene los identificadores de los pedidos asociados a un cliente específico.
     * 
     * Este método permite consultar el historial de pedidos de un cliente.
     * 
     * @param idUsuario identificador del cliente
     * @return lista de identificadores de pedidos
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<Integer> obtenerIdsPedidosPorCliente(int idUsuario) throws PersistenciaException;
}