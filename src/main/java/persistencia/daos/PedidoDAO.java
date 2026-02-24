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
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoResumen;
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
    public List<PedidoResumen> obtenerPedidosTabla() throws PersistenciaException {
        System.out.println("llegue");

        String sql = """
        SELECT 
            p.idPedido,
            COALESCE(CONCAT(c.nombres, ' ', c.apellidoPaterno), '') AS cliente,
            COALESCE(t.telefono, '') AS telefono,
            p.fechaHoraPedido AS fechaHora,
            p.estadoActual AS estado,
            CASE 
                WHEN pe.idPedido IS NOT NULL THEN 'EXPRESS'
                WHEN pp.idPedido IS NOT NULL THEN 'PROGRAMADO'
                ELSE 'NORMAL'
            END AS tipo,
            pe.folio
        FROM Pedidos p
        LEFT JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
        LEFT JOIN PedidosExpress pe ON p.idPedido = pe.idPedido
        LEFT JOIN Clientes c ON pp.idUsuario IS NOT NULL AND c.idUsuario = pp.idUsuario
        LEFT JOIN TelefonosClientes t ON t.idCliente = c.idUsuario
    """;

        List<PedidoResumen> lista = new ArrayList<>();

        System.out.println("sql aft");

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("idPedido");
                String cliente = rs.getString("cliente");
                String telefono = rs.getString("telefono");
                LocalDateTime fechaHora = rs.getTimestamp("fechaHora").toLocalDateTime();
                String estado = rs.getString("estado");
                String tipo = rs.getString("tipo");
                String folio = rs.getString("folio"); // solo para EXPRESS, null para otros
                double total = 0; // Aquí podrías calcular el total si lo sumas desde DetallesPedidos

                lista.add(new PedidoResumen(idPedido, folio, cliente, telefono, fechaHora, total, estado, tipo));
            }

            System.out.println("Pedidos obtenidos: " + lista.size());
            System.out.println(lista);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener pedidos para la tabla: " + e.getMessage(), e);
        }

        return lista;
    }

    @Override
    public List<PedidoResumen> obtenerPedidosFiltrados(String filtro) throws PersistenciaException {

        List<PedidoResumen> lista = new ArrayList<>();

        String sql = """
    SELECT 
        p.idPedido,
        COALESCE(pe.folio, pp.numPedido) AS folio,
        CONCAT(c.nombres, ' ', c.apellidoPaterno) AS cliente,
        t.telefono,
        p.fechaHoraPedido,
        COALESCE(SUM(dp.cantidad * piz.precio), 0) AS total,
        p.estadoActual
    FROM Pedidos p
    LEFT JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
    LEFT JOIN PedidosExpress pe ON p.idPedido = pe.idPedido
    LEFT JOIN Clientes c ON pp.idUsuario = c.idUsuario
    LEFT JOIN TelefonosClientes t ON t.idCliente = c.idUsuario
    LEFT JOIN DetallesPedidos dp ON p.idPedido = dp.idPedido
    LEFT JOIN Pizzas piz ON dp.idPizza = piz.idPizza
    WHERE 
        CONCAT(IFNULL(c.nombres,''),' ',IFNULL(c.apellidoPaterno,'')) LIKE ?
        OR pe.folio LIKE ?
        OR CAST(pp.numPedido AS CHAR) LIKE ?
        OR t.telefono LIKE ?
    GROUP BY 
        p.idPedido,
        folio,
        cliente,
        t.telefono,
        p.fechaHoraPedido,
        p.estadoActual
    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + filtro + "%";

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                PedidoResumen pt = new PedidoResumen(
                        rs.getInt("idPedido"),
                        rs.getString("folio"),
                        rs.getString("cliente") != null ? rs.getString("cliente") : "Público General",
                        rs.getString("telefono") != null ? rs.getString("telefono") : "N/A",
                        rs.getTimestamp("fechaHoraPedido").toLocalDateTime(),
                        rs.getDouble("total"),
                        rs.getString("estadoActual"),
                        rs.getString("tipo")
                );

                lista.add(pt);
            }

            return lista;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al filtrar pedidos", ex);
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
    public PedidoDetalleDTO obtenerDetallePedido(int idPedido) throws PersistenciaException {

        String sql = """
        SELECT 
            p.idPedido,
            COALESCE(pe.folio, pp.numPedido, 'N/A') AS folio,
            COALESCE(CONCAT(c.nombres, ' ', c.apellidoPaterno), 'Público General') AS nombreCliente,
            dp.cantidad,
            piz.nombre AS nombreProducto,
            (dp.cantidad * piz.precio) AS subtotal
        FROM Pedidos p
        LEFT JOIN PedidosProgramados pp ON p.idPedido = pp.idPedido
        LEFT JOIN PedidosExpress pe ON p.idPedido = pe.idPedido
        LEFT JOIN Clientes c ON pp.idUsuario = c.idUsuario
        LEFT JOIN DetallesPedidos dp ON dp.idPedido = p.idPedido
        LEFT JOIN Pizzas piz ON dp.idPizza = piz.idPizza
        WHERE p.idPedido = ?
    """;

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ResultSet rs = ps.executeQuery();

            List<DetallePedidoDTO> listaDetalles = new ArrayList<>();
            int id = -1;
            String folio = null;
            String nombreCliente = null;
            double total = 0;

            while (rs.next()) {
                id = rs.getInt("idPedido");
                folio = rs.getString("folio");
                nombreCliente = rs.getString("nombreCliente");

                int cantidad = rs.getInt("cantidad");
                String nombreProducto = rs.getString("nombreProducto");
                double subtotal = rs.getDouble("subtotal");

                total += subtotal;

                if (nombreProducto != null) {
                    listaDetalles.add(new DetallePedidoDTO(cantidad, nombreProducto, subtotal));
                }
            }

            if (id == -1) {
                return null; // No existe pedido
            }

            if (listaDetalles.isEmpty()) {
                listaDetalles.add(new DetallePedidoDTO(0, "Sin productos", 0));
            }

            return new PedidoDetalleDTO(id, folio, nombreCliente, total, listaDetalles);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener detalle del pedido: " + e.getMessage(), e);
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
    public void cambiarEstado(int idPedido, String nuevoEstado) throws PersistenciaException {

        try {

            CallableStatement cs = conexionBD.crearConexion().prepareCall("{CALL SP_CAMBIAR_ESTADO(?, ?)}");

            cs.setInt(1, idPedido);
            cs.setString(2, nuevoEstado);

            cs.execute();

        } catch (SQLException e) {

            throw new PersistenciaException(
                    "Error al cambiar el estado del pedido",
                    e
            );

        }
    }
}
