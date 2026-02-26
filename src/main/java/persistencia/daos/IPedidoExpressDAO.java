package persistencia.daos;

import java.util.List;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia para la entidad
 * PedidoExpress.
 *
 * Se encarga de establecer los metodos que permiten interactuar con la base de
 * datos.
 *
 * @author Brian
 */
public interface IPedidoExpressDAO {

    /**
     * Metodo que permite agregar un PedidoExpress a la base de datos.
     *
     * @param pedidoExpress Objeto PedidoExpress con la informacion a registrar.
     * @return PedidoExpress con el id generado en base de datos.
     * @throws PersistenciaException Si ocurre un error durante la insercion.
     */
    PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException;

    /**
     * Metodo que permite obtener un PedidoExpress por su id.
     *
     * @param idPedidoExpress Identificador del pedido.
     * @return PedidoExpress encontrado.
     * @throws PersistenciaException Si ocurre un error en la consulta.
     */
    PedidoExpress obtenerPedidoExpress(int idPedidoExpress) throws PersistenciaException;

    /**
     * Metodo que permite obtener todos los pedidos express registrados.
     *
     * @return Lista de objetos PedidoExpress.
     * @throws PersistenciaException Si ocurre un error en la consulta.
     */
    List<PedidoExpress> obtenerPedidosExpress() throws PersistenciaException;

    int obtenerFolio() throws PersistenciaException;

    /**
     *
     * @param pin
     * @return
     * @throws PersistenciaException
     */
    boolean obtenerPinValido(String pin) throws PersistenciaException;
    
    PedidoExpress actualizarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException;
    
     public void insertarDetalle(int idPedido, int idPizza, int cantidad, String nota) throws PersistenciaException;
    
    public void actualizarPedidoExpressNoRecolectado() throws PersistenciaException;
    
    int obtenerCantidadPedidosPorToken(String token) throws PersistenciaException;
}
