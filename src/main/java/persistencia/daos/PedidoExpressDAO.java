package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase que implementa las operaciones de persistencia para la entidad
 * PedidoExpress.
 *
 * Contiene la logica necesaria para ejecutar consultas y procedimientos
 * almacenados en la base de datos.
 *
 * @author Brian
 */
public class PedidoExpressDAO implements IPedidoExpressDAO {

    private final IConexionBD conexionBD;
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que recibe la conexion a la base de datos.
     *
     * @param conexionBD Objeto que permite crear conexiones.
     */
    public PedidoExpressDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Metodo que ejecuta el procedimiento almacenado para registrar un
     * PedidoExpress.
     *
     * @param pedidoExpress Objeto con la informacion a registrar.
     * @return PedidoExpress con el id generado.
     * @throws PersistenciaException Si ocurre un error en la ejecucion del SP.
     */
    @Override
    public PedidoExpress agregarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException {
        String sp = """
                    CALL SP_REGISTAR_PEDIDO_EXPRESS(
                        ?,
                        ?,
                        ?,
                        ?
                    )
                    """;

        try (Connection conexion = conexionBD.crearConexion(); CallableStatement cs = conexion.prepareCall(sp)) {
            cs.setString(1, pedidoExpress.getNota());
            cs.setInt(2, pedidoExpress.getFolio());
            cs.setString(3, pedidoExpress.getPin());
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            int idGeneradoPedidoExpress = cs.getInt(4);
            pedidoExpress.setIdPedido(idGeneradoPedidoExpress);
            return pedidoExpress;

        } catch (SQLException e) {
            LOG.severe(() -> "ERROR: Al intentar agregar un pedido express con el SP " + e);
            throw new PersistenciaException("ERROR: Al intentar agregar un pedido express con el SP " + e);
        }
    }

    /**
     * Metodo que obtiene un PedidoExpress por su id.
     *
     * @param idPedidoExpress Identificador del pedido.
     * @return PedidoExpress encontrado.
     * @throws PersistenciaException Si ocurre un error en la consulta.
     */
    @Override
    public PedidoExpress obtenerPedidoExpress(int idPedidoExpress) throws PersistenciaException {
        String sentenciaSQL = """
                              SELECT 
                              	idPedido, 
                                  folio, 
                                  PIN
                              FROM pedidosexpress
                              WHERE idPedido = ?
                              """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
            ps.setInt(1, idPedidoExpress);

            try (ResultSet rs = ps.executeQuery()) {
                PedidoExpress pedidoEx = extraerPedidoExpress(rs);
                return pedidoEx;
            }

        } catch (SQLException ex) {
            LOG.severe(() -> "ERROR al obtener el pedido express." + ex);
            throw new PersistenciaException("ERROR al obtener el pedido express." + ex);
        }
    }

    /**
     * Metodo que actualiza automaticamente los pedidos express que no fueron
     * recolectados en el tiempo limite (20 minutos).
     *
     * Cambia el estado a NO_ENTREGADO si se cumple la condicion.
     *
     * @throws PersistenciaException Si ocurre un error durante el update.
     */
    @Override
    public void actualizarPedidoExpressNoRecolectado() throws PersistenciaException {
        String sentenciaSQL = """
                              UPDATE Pedidos
                              SET estadoActual = 'NO_ENTREGADO'
                              WHERE estadoActual = 'PENDIENTE'
                              AND TIMESTAMPDIFF(MINUTE, fechaHoraPedido, NOW()) >= 20
                              AND tipo = 'EXPRESS'
                              """;
        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                LOG.info("Se ha cambiado el estado del pedido a 'NO_ENTREGADO'.");
            } else {
                LOG.info("El pedido todavía se encuentra dentro de los 20 minutos de tolerancia.");
            }
        } catch (SQLException ex) {
            LOG.severe(() -> "Ha fallado el update de pedidos." + ex);
            throw new PersistenciaException("Ha fallado el update de pedidos." + ex);
        }
    }

    /**
     * Metodo que obtiene todos los pedidos express registrados.
     *
     * @return Lista de objetos PedidoExpress.
     * @throws PersistenciaException Si ocurre un error en la consulta.
     */
    @Override
    public List<PedidoExpress> obtenerPedidosExpress() throws PersistenciaException {

        List<PedidoExpress> pedidosEx = new ArrayList<>();

        String sentenciaSQL = """
                              SELECT 
                              	idPedido, 
                                  folio, 
                                  PIN
                              FROM pedidosexpress
                              """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoExpress pedidoEx = extraerPedidoExpress(rs);
                    pedidosEx.add(pedidoEx);
                }
            }
            return pedidosEx;

        } catch (SQLException ex) {
            LOG.severe(() -> "ERROR al obtener el pedido express." + ex);
            throw new PersistenciaException("ERROR al obtener el pedido express." + ex);
        }
    }

    /**
     * Metodo auxiliar que permite convertir un ResultSet en un objeto
     * PedidoExpress.
     *
     * @param rs ResultSet con los datos obtenidos de la consulta.
     * @return Objeto PedidoExpress con los datos mapeados.
     * @throws PersistenciaException Si ocurre un error al extraer los datos.
     */
    public PedidoExpress extraerPedidoExpress(ResultSet rs) throws PersistenciaException {
        try {
            PedidoExpress pedidoEx = new PedidoExpress();
            pedidoEx.setIdPedido(rs.getInt("idPedido"));
            pedidoEx.setFolio(rs.getInt("folio"));
            pedidoEx.setPin(rs.getString("PIN"));

            return pedidoEx;

        } catch (SQLException ex) {
            LOG.severe(() -> "Error al extraer el pedido express: " + ex);
            throw new PersistenciaException("Error al obtener el pedido express" + ex);
        }
    }

    /**
     *
     * @return @throws PersistenciaException
     * @throws persistencia.excepciones.PersistenciaException
     */
    @Override
    public int obtenerFolio() throws PersistenciaException {
        String sql = """
                     SELECT MAX(folio) FROM pedidosexpress
                     """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int max = rs.getInt(1);

                // Si la tabla está vacía, max devuelve null,
                if (rs.wasNull()) {
                    return 0;
                }

                return max;
            }

            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("No fue posible obtener el folio", e);
        }
    }

    @Override
    public boolean obtenerPinValido(String pin) throws PersistenciaException {
        String sentenciaSQL = """
                              SELECT PIN
                              FROM pedidosexpress
                              """;
        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String codigoHash = rs.getString("PIN");
                //Aqui valida si el pin es igual a uno que este guardado.
                if (BCrypt.checkpw(pin, codigoHash)) {
                    return true;
                }

            }
            return false;
        } catch (SQLException ex) {
            LOG.severe("No fue posible obtener el pin.");
            throw new PersistenciaException("No fue posible obtener el pin" + ex);
        }
    }

    @Override
    public PedidoExpress actualizarPedidoExpress(PedidoExpress pedidoExpress) throws PersistenciaException {
        String sentenciaSQL = """
                              UPDATE pedidos
                              set estadoActual = ?
                              WHERE idPedido = ?;
                              """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sentenciaSQL)) {
            ps.setString(1, pedidoExpress.getEstadoActual());
            ps.setInt(2, pedidoExpress.getIdPedido());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                LOG.info(() -> "Se ha cambiado el estado del pedido a " + pedidoExpress.getEstadoActual());
            } else {
                LOG.info("El pedido no se ha podido actualizar");
            }

            return pedidoExpress;
        } catch (SQLException ex) {
            LOG.severe(() -> "No fue posible actualizar el pedido" + ex);
            throw new PersistenciaException("No fue posible actualizar el pedido" + ex);

        }
    }
    
    @Override
    public void insertarDetalle(int idPedido, int idPizza, int cantidad, String nota) throws PersistenciaException {
        String sql = "INSERT INTO DetallesPedidos (idPedido, idPizza, cantidad, nota) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            ps.setInt(2, idPizza);
            ps.setInt(3, cantidad);
            ps.setString(4, nota != null ? nota : "");
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al insertar detalle de pedido", ex);
            throw new PersistenciaException("Error al insertar detalle de pedido", ex);
        }
    }
}