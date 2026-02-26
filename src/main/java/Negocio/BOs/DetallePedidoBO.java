package Negocio.BOs;

import Negocio.DTOs.DetallePedidoDTO;
import Negocio.excepciones.NegocioException;
import java.sql.SQLException;
import persistencia.daos.DetallePedidoDAO;

/**
 * Clase de lógica de negocio encargada de gestionar los detalles asociados a un
 * pedido dentro del sistema.
 *
 * <p>
 * Esta clase pertenece a la capa de negocio (BO - Business Object) y se encarga
 * de coordinar la creación de los detalles de un pedido antes de enviarlos a la
 * capa de persistencia.
 * </p>
 *
 * <h2>Responsabilidades principales:</h2>
 * <ul>
 * <li>Crear objetos DetallePedidoDTO.</li>
 * <li>Asignar los datos correspondientes al detalle.</li>
 * <li>Delegar la inserción del detalle a la capa DAO.</li>
 * <li>Manejar excepciones SQL y convertirlas en excepciones de negocio.</li>
 * </ul>
 *
 * <p>
 * Esta clase funciona como intermediaria entre la capa de presentación y la
 * capa de persistencia, garantizando el correcto flujo de información.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez
 * @author Alejandra Leal Armenta
 * @author Paulina Michel Guevara Cervantes
 */
public class DetallePedidoBO implements IDetallePedidoBO {

    private DetallePedidoDAO detallePedidoDAO;

    /**
     * Constructor que recibe la dependencia del DAO.
     *
     * @param detallePedidoDAO Instancia encargada de la persistencia de los
     * detalles del pedido.
     */
    public DetallePedidoBO(DetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = detallePedidoDAO;
    }

    /**
     * Agrega un nuevo detalle a un pedido.
     *
     * <p>
     * Crea un objeto DetallePedidoDTO con la información proporcionada y lo
     * envía a la capa de persistencia para su almacenamiento.
     * </p>
     *
     * @param idPedido Identificador del pedido.
     * @param idPizza Identificador de la pizza.
     * @param cantidad Cantidad solicitada.
     * @param nota Nota adicional asociada al producto.
     * @throws NegocioException Si ocurre un error durante la inserción.
     */
    @Override
    public void agregarDetallePedido(int idPedido, int idPizza, int cantidad, String nota) throws NegocioException {
        try {
            DetallePedidoDTO detalle = new DetallePedidoDTO();
            detalle.setIdPedido(idPedido);
            detalle.setIdPizza(idPizza);
            detalle.setCantidad(cantidad);
            detalle.setNota(nota);
            detallePedidoDAO.insertarDetalle(detalle);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo agregar el detalle del pedido: " + e.getMessage());
        }
    }
}
