package persistencia.daos;

import java.util.List;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link PedidoExpress}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object) y establece el contrato
 * que deben cumplir las clases encargadas de gestionar los pedidos express en la base de datos.
 * 
 * Permite realizar operaciones como registrar, actualizar, consultar, validar PIN,
 * insertar detalles y gestionar el estado de los pedidos express.
 * 
 * Facilita el desacoplamiento entre la lógica de negocio y el acceso a datos,
 * mejorando la mantenibilidad y escalabilidad del sistema.
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPedidoExpressDAO {

    /**
     * Inserta un nuevo pedido express en la base de datos.
     * 
     * Este método registra un pedido express y genera automáticamente su identificador único.
     *
     * @param pedidoExpress objeto {@link PedidoExpress} que contiene la información del pedido
     * @return objeto {@link PedidoExpress} con el identificador generado
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException;

    /**
     * Obtiene un pedido express mediante su identificador único.
     *
     * @param idPedidoExpress identificador del pedido express
     * @return objeto {@link PedidoExpress} encontrado
     * @throws PersistenciaException si ocurre un error durante la consulta o no existe el pedido
     */
    PedidoExpress obtenerPedidoExpress(int idPedidoExpress) throws PersistenciaException;

    /**
     * Obtiene todos los pedidos express registrados en la base de datos.
     *
     * @return lista de objetos {@link PedidoExpress}
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    List<PedidoExpress> obtenerPedidosExpress() throws PersistenciaException;

    /**
     * Obtiene el siguiente folio disponible para un pedido express.
     * 
     * El folio se utiliza como identificador adicional para la validación y seguimiento del pedido.
     *
     * @return folio generado o disponible
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    int obtenerFolio() throws PersistenciaException;

    /**
     * Valida si un PIN proporcionado corresponde a un pedido express válido.
     * 
     * Este método se utiliza como mecanismo de seguridad para verificar la autenticidad
     * del pedido al momento de su recolección o consulta.
     *
     * @param pin PIN del pedido express
     * @return true si el PIN es válido, false en caso contrario
     * @throws PersistenciaException si ocurre un error durante la validación
     */
    boolean obtenerPinValido(String pin) throws PersistenciaException;

    /**
     * Actualiza la información de un pedido express existente.
     *
     * @param pedidoExpress objeto {@link PedidoExpress} con la información actualizada
     * @return objeto {@link PedidoExpress} actualizado
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    PedidoExpress actualizarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException;

    /**
     * Inserta el detalle de un pedido express en la base de datos.
     * 
     * Este método registra los productos asociados al pedido express.
     *
     * @param idPedido identificador del pedido express
     * @param idPizza identificador de la pizza
     * @param cantidad cantidad solicitada
     * @param nota nota adicional del pedido
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    void insertarDetalle(int idPedido, int idPizza, int cantidad, String nota) throws PersistenciaException;

    /**
     * Actualiza automáticamente el estado de los pedidos express que no han sido recolectados
     * dentro del tiempo establecido.
     * 
     * Generalmente cambia el estado a "NO_ENTREGADO" o equivalente.
     *
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    void actualizarPedidoExpressNoRecolectado() throws PersistenciaException;

    /**
     * Obtiene la cantidad de pedidos express asociados a un token específico.
     * 
     * El token puede representar una sesión o identificador temporal del cliente.
     *
     * @param token identificador único del cliente o sesión
     * @return cantidad de pedidos express asociados al token
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    int obtenerCantidadPedidosPorToken(String token) throws PersistenciaException;
}