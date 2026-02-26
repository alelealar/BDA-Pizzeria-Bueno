package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia
 * relacionadas con los detalles de los pedidos en la base de datos.
 * 
 * Esta clase permite insertar los productos que pertenecen a un pedido,
 * incluyendo información como la pizza solicitada, cantidad y notas adicionales.
 * 
 * Utiliza el patrón DAO (Data Access Object) para encapsular la lógica
 * de acceso a datos y mantener la separación entre la capa de negocio
 * y la capa de persistencia.
 * 
 * La conexión a la base de datos es inyectada mediante el constructor
 * para reducir el acoplamiento y facilitar pruebas unitarias.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class DetallePedidoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     * Permite ejecutar operaciones SQL sobre la tabla DetallesPedidos.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger utilizado para registrar información relevante y errores
     * durante las operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @param conexionBD objeto que proporciona la conexión a la base de datos
     */
    public DetallePedidoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta un nuevo detalle de pedido en la base de datos.
     * 
     * Este método registra una pizza dentro de un pedido específico,
     * incluyendo la cantidad solicitada y cualquier nota adicional
     * proporcionada por el cliente.
     *
     * @param detalle objeto {@link DetallePedidoDTO} que contiene la información
     *                del detalle a insertar, como id del pedido, id de la pizza,
     *                cantidad y nota.
     * @throws SQLException si ocurre un error durante la ejecución de la sentencia SQL
     */
    public void insertarDetalle(DetallePedidoDTO detalle) throws SQLException {

        String sql = """
            INSERT INTO DetallesPedidos (idPedido, idPizza, cantidad, nota)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = conexionBD.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdPizza());
            ps.setInt(3, detalle.getCantidad());
            ps.setString(4, detalle.getNota());

            ps.executeUpdate();
        }
    }
}
