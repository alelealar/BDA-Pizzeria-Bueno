package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import Negocio.DTOs.PedidoDTO;
import Negocio.DTOs.PedidoDetalleDTO;
import Negocio.DTOs.PedidoTablaDTO;
import Negocio.excepciones.NegocioException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.DetallePedido;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoResumen;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * Clase que implementa las operaciones de persistencia relacionadas con la
 * entidad Pedido.
 *
 * Se encarga de insertar, actualizar y consultar pedidos en la base de datos.
 *
 * Forma parte de la capa de persistencia y utiliza una conexión inyectada para
 * reducir el acoplamiento.
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PedidoDAO implements IPedidoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     *
     * Se inyecta por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public PedidoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Inserta un nuevo pedido en la base de datos.
     *
     * @param pedido objeto Pedido que contiene la información a guardar
     * @return el pedido con el ID generado por la base de datos
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    @Override
    public Pedido agregarPedido(Pedido pedido) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO pedidos(
                                estadoActual, fechaHoraPedido, fechaHoraEntrega, nota
                            ) VALUES(
                               ?, ?, ?, ?
                            )
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getEstadoActual());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(pedido.getFechaHoraPedido()));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(pedido.getFechaHoraEntrega()));
            ps.setString(4, pedido.getNota());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                LOG.warning(() -> "No se pudo insertar el pedido: " + pedido.toString());
                throw new PersistenciaException("No se logró agregar el pedido.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1));
                } else {
                    throw new PersistenciaException("Error al obtener el ID generado del nuevo pedido.");
                }
            }

            LOG.info(() -> "Pedido insertado con éxito. ID: " + pedido.getIdPedido());
            return pedido;

        } catch (SQLException ex) {
            LOG.severe(() -> "Error SQL al insertar el pedido " + ex);
            throw new PersistenciaException("Error al insertar el pedido en la base de datos", ex);
        }
    }

    /**
     * Actualiza la información de un pedido existente en la base de datos.
     *
     * @param pedido objeto Pedido con los datos actualizados
     * @return el pedido actualizado
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    @Override
    public Pedido actualizarPedido(Pedido pedido) throws PersistenciaException {
        String comandoSQL = """
                            UPDATE pedidos
                            SET estadoActual = ?,
                                fechaHoraPedido = ?,
                                fechaHoraEntrega = ?,
                                nota = ?
                            WHERE idPedido = ?;
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, pedido.getEstadoActual());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(pedido.getFechaHoraPedido()));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(pedido.getFechaHoraEntrega()));
            ps.setString(4, pedido.getNota());
            ps.setInt(5, pedido.getIdPedido());

            int filasActualizadas = ps.executeUpdate();
            if (filasActualizadas == 0) {
                LOG.warning(() -> "No se pudo actualizar el pedido: " + pedido.toString());
                throw new PersistenciaException("No se logró actualizar el pedido.");
            }

            LOG.info(() -> "Pedido actualizado con éxito. Descripción: " + pedido.toString());
            return pedido;

        } catch (SQLException ex) {
            LOG.severe(() -> "Error SQL al actualizar el pedido " + ex);
            throw new PersistenciaException("Error al actualizar el pedido en la base de datos", ex);
        }
    }

    /**
     * Busca un pedido en la base de datos utilizando su identificador.
     *
     * @param id identificador del pedido a buscar
     * @return objeto Pedido encontrado
     * @throws PersistenciaException si el pedido no existe o ocurre un error
     */
    @Override
    public Pedido buscarPedidoPorId(int id) throws PersistenciaException {
        String comandoSQL = """
                            SELECT 
                                idPedido, estadoActual, fechaHoraPedido, fechaHoraEntrega, nota
                            FROM pedidos
                            WHERE idPedido = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    LOG.log(Level.WARNING, "No se encontró el pedido con id {0}", id);
                    throw new PersistenciaException("No existe el pedido con el ID proporcionado.");
                }

                return extraerPedido(rs);
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al buscar el pedido " + ex);
            throw new PersistenciaException("Error al buscar el pedido en la base de datos", ex);
        }
    }

    /**
     * Extrae la información de un ResultSet y construye un objeto Pedido con
     * los datos obtenidos.
     *
     * @param rs resultado de la consulta ejecutada
     * @return objeto Pedido con la información extraída
     * @throws PersistenciaException si ocurre un error al leer los datos
     */
    public Pedido extraerPedido(ResultSet rs) throws PersistenciaException {
        try {
            Pedido pedido = new Pedido();
            pedido.setIdPedido(rs.getInt("idPedido"));
            pedido.setEstadoActual(rs.getString("estadoActual"));

            Date fechaHoraPedido = rs.getDate("fechaHoraPedido");
            Date fechaHoraEntrega = rs.getDate("fechaHoraEntrega");

            pedido.setFechaHoraPedido(rs.getTimestamp("fechaHoraPedido").toLocalDateTime());
            pedido.setFechaHoraEntrega(rs.getTimestamp("fechaHoraEntrega").toLocalDateTime());
            pedido.setNota(rs.getString("nota"));

            if ("PROGRAMADO".equalsIgnoreCase(rs.getString("tipo"))) {
                pedido.setTipo(Pedido.Tipo.PROGRAMADO);
            } else {
                pedido.setTipo(Pedido.Tipo.EXPRESS);
            }

            return pedido;

        } catch (SQLException ex) {
            LOG.severe("Error al extraer el pedido " + ex);
            throw new PersistenciaException("Error al extraer el pedido.");
        }
    }

    @Override
    public List<Pedido> consultarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) throws PersistenciaException {
        List<Pedido> pedidos = new ArrayList<>();
        String comandoSQL = """
                            SELECT
                                idPedido, 
                                estadoActual, 
                                fechaHoraPedido, 
                                fechaHoraEntrega, 
                                nota, 
                                tipo
                            FROM pedidos
                            WHERE fechaHoraPedido >= ?
                              AND fechaHoraPedido < ?;
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setObject(1, fechaInicio);
            ps.setObject(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = extraerPedido(rs);
                    pedidos.add(p);
                }
            }
            return pedidos;

        } catch (SQLException ex) {
            LOG.severe("Error al consultar pedidos por rango de fechas: " + ex);
            throw new PersistenciaException("Error al consultar pedidos por rango de fechas");
        }
    }

    @Override
    public List<Pedido> obtenerPedidos() throws PersistenciaException {
        List<Pedido> pedidos = new ArrayList<>();

        String comandoSQL = """
                        SELECT 
                            idPedido,
                            estadoActual,
                            fechaHoraPedido,
                            fechaHoraEntrega,
                            nota,
                            tipo
                        FROM pedidos;
                        """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = extraerPedido(rs);
                pedidos.add(pedido);
            }

            return pedidos;

        } catch (SQLException ex) {
            LOG.severe("Error al obtener los pedidos: " + ex);
            throw new PersistenciaException("Error al consultar los pedidos.", ex);
        }
    }

    @Override
    public List<PedidoResumen> obtenerPedidosFiltrados(String filtro)
            throws PersistenciaException {

        List<PedidoResumen> lista = new ArrayList<>();

        String sql = """
        SELECT 
            p.idPedido,
            p.tipo,
            COALESCE(pe.folio, pp.numPedido) AS folio,
            CONCAT(IFNULL(c.nombres,''),' ',IFNULL(c.apellidoPaterno,'')) AS cliente,
            tel.telefono,
            p.fechaHoraPedido,
            (
                SELECT SUM(dp.cantidad * piz.precio)
                FROM DetallesPedidos dp
                JOIN Pizzas piz ON dp.idPizza = piz.idPizza
                WHERE dp.idPedido = p.idPedido
            ) AS total,
            p.estadoActual
        FROM Pedidos p
        LEFT JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
        LEFT JOIN PedidosExpress pe ON p.idPedido = pe.idPedido
        LEFT JOIN Clientes c ON c.idUsuario = pp.idUsuario
        LEFT JOIN (
            SELECT idCliente, MIN(telefono) AS telefono
            FROM TelefonosClientes
            GROUP BY idCliente
        ) tel ON tel.idCliente = c.idUsuario
        WHERE (
               COALESCE(pe.folio, pp.numPedido) LIKE ?
            OR CONCAT(IFNULL(c.nombres,''),' ',IFNULL(c.apellidoPaterno,'')) LIKE ?
            OR tel.telefono LIKE ?
            OR p.tipo LIKE ?
            OR p.estadoActual LIKE ?
        )
        ORDER BY p.fechaHoraPedido DESC
        """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + filtro + "%";

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like); // TELEFONO
            ps.setString(4, like);
            ps.setString(5, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String cliente = rs.getString("cliente");
                if (cliente == null || cliente.trim().isEmpty()) {
                    cliente = "Público General";
                }

                String telefono = rs.getString("telefono");
                if (telefono == null || telefono.trim().isEmpty()) {
                    telefono = "N/A";
                }

                Double total = rs.getDouble("total");
                if (rs.wasNull()) {
                    total = 0.0;
                }

                PedidoResumen pedido = new PedidoResumen(
                        rs.getInt("idPedido"),
                        rs.getString("folio"),
                        cliente,
                        telefono,
                        rs.getTimestamp("fechaHoraPedido").toLocalDateTime(),
                        total,
                        rs.getString("estadoActual"),
                        rs.getString("tipo")
                );

                lista.add(pedido);
            }

            return lista;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al filtrar pedidos", ex);
        }
    }

    @Override
    public void cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException {

        String sql = "{CALL SP_CAMBIAR_ESTADO(?, ?)}";

        try (Connection con = conexionBD.crearConexion(); CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idPedido);
            cs.setString(2, nuevoEstado);

            cs.execute();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al cambiar estado", e);
        }
    }
//    @Override
//    public void cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException {
//
//        String sql = "UPDATE Pedidos SET estadoActual = ? WHERE idPedido = ?";
//
//        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
//
//            ps.setString(1, nuevoEstado);
//            ps.setInt(2, idPedido);
//
//            ps.executeUpdate();
//
//        } catch (SQLException ex) {
//            throw new PersistenciaException("Error al cambiar estado del pedido");
//        }
//    }

    @Override
    public Pedido obtenerDetallePedido(int idPedido) throws PersistenciaException {

        String sql = """
        SELECT 
            p.idPedido,
            p.tipo,
            p.estadoActual,
            p.fechaHoraPedido,
            dp.idDetallesPedidos,
            dp.cantidad,
            dp.nota,
            piz.idPizza,
            piz.nombre,
            piz.tamaño,
            piz.descripcion,
            piz.precio
        FROM Pedidos p
        INNER JOIN DetallesPedidos dp ON p.idPedido = dp.idPedido
        INNER JOIN Pizzas piz ON dp.idPizza = piz.idPizza
        WHERE p.idPedido = ?
    """;

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            Pedido pedido = null;

            while (rs.next()) {

                if (pedido == null) {
                    pedido = new Pedido();
                    pedido.setIdPedido(rs.getInt("idPedido"));
                    pedido.setEstadoActual(rs.getString("estadoActual"));
                    pedido.setTipo(Pedido.Tipo.valueOf(rs.getString("tipo").toUpperCase()));
                    Timestamp ts = rs.getTimestamp("fechaHoraPedido");
                    if (ts != null) {
                        pedido.setFechaHoraPedido(ts.toLocalDateTime());
                    }
                    pedido.setDetalles(new ArrayList<>());
                }

                // Crear pizza
                Pizza pizza = new Pizza();
                pizza.setIdPizza(rs.getInt("idPizza"));
                pizza.setNombre(rs.getString("nombre"));
                pizza.setTamanio(rs.getString("tamaño"));
                pizza.setDescripcion(rs.getString("descripcion"));
                pizza.setPrecio(rs.getDouble("precio"));

                // Crear detalle
                DetallePedido detalle = new DetallePedido();
                detalle.setIdDetallePedido(rs.getInt("idDetallesPedidos"));
                detalle.setCantidad(rs.getInt("cantidad"));
                detalle.setNota(rs.getString("nota"));
                detalle.setPrecio(rs.getDouble("precio"));
                detalle.setPizza(pizza);
                detalle.setPedido(pedido);

                pedido.getDetalles().add(detalle);
            }

            return pedido;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener detalle del pedido", e);
        }
    }

    @Override
    public boolean validarFolioYPIN(int idPedido, String folio, String pin) throws PersistenciaException {

        String sql = "SELECT PIN FROM PedidosExpress WHERE idPedido = ? AND folio = ?";

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.setString(2, folio);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return false; // No existe pedido con ese folio
                }

                String pinHash = rs.getString("PIN");

                if (!BCrypt.checkpw(pin, pinHash)) {
                    throw new PersistenciaException("El PIN no coincide");
                }

                return true;
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error validando folio y PIN", e);
        }
    }

    @Override
    public List<Integer> obtenerIdsPedidosPorCliente(int idUsuario) throws PersistenciaException {

        String sql = """
        SELECT p.idPedido
        FROM Pedidos p
        INNER JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
        WHERE pp.idUsuario = ?
        ORDER BY p.fechaHoraPedido DESC
    """;

        List<Integer> ids = new ArrayList<>();

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ids.add(rs.getInt("idPedido"));
            }

            return ids;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener pedidos del cliente", e);
        }
    }

    @Override
    public List<PedidoResumen> obtenerPedidosTabla() throws PersistenciaException {

        String sql = """
    SELECT 
        p.idPedido,
        p.tipo,
        COALESCE(CONCAT(c.nombres, ' ', c.apellidoPaterno), '') AS cliente,
        COALESCE(t.telefonos, '') AS telefono,
        p.fechaHoraPedido AS fechaHora,
        p.estadoActual AS estado,
        pe.folio,
        pp.numPedido,
        COALESCE((
            SELECT SUM(dp.cantidad * pz.precio)
            FROM DetallesPedidos dp
            INNER JOIN Pizzas pz ON dp.idPizza = pz.idPizza
            WHERE dp.idPedido = p.idPedido
        ), 0) AS total
    FROM Pedidos p
    LEFT JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
    LEFT JOIN PedidosExpress pe ON p.idPedido = pe.idPedido
    LEFT JOIN Clientes c ON c.idUsuario = pp.idUsuario
    LEFT JOIN (
        SELECT idCliente, GROUP_CONCAT(telefono SEPARATOR ', ') AS telefonos
        FROM TelefonosClientes
        GROUP BY idCliente
    ) t ON t.idCliente = c.idUsuario
    ORDER BY p.fechaHoraPedido DESC
    """;

        List<PedidoResumen> lista = new ArrayList<>();

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idPedido = rs.getInt("idPedido");

                String cliente = rs.getString("cliente");
                if (cliente == null || cliente.trim().isEmpty()) {
                    cliente = "Público General";
                }

                String telefono = rs.getString("telefono");
                if (telefono == null || telefono.trim().isEmpty()) {
                    telefono = "N/A";
                }

                LocalDateTime fechaHora
                        = rs.getTimestamp("fechaHora").toLocalDateTime();

                String estado = rs.getString("estado");
                String tipo = rs.getString("tipo");

                String folio = rs.getString("folio");
                int numPedido = rs.getInt("numPedido");

                double total = rs.getDouble("total");

                lista.add(new PedidoResumen(
                        idPedido,
                        tipo.equals("EXPRESS") ? folio : String.valueOf(numPedido),
                        cliente,
                        telefono,
                        fechaHora,
                        total,
                        estado,
                        tipo
                ));
            }

        } catch (SQLException e) {

            throw new PersistenciaException(
                    "Error al obtener pedidos para la tabla: " + e.getMessage(), e);
        }

        return lista;
    }
}