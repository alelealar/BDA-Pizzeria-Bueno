
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import Negocio.excepciones.NegocioException;
import java.lang.ProcessBuilder.Redirect.Type;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.dominio.Cupon;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PedidoProgramadoDAO implements IPedidoProgramadoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     *
     * Se inyecta por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final IConexionBD conexionBD;
    private final CuponDAO cuponDAO;
    private final ClienteDAO clienteDAO;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public PedidoProgramadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
        this.cuponDAO = new CuponDAO(conexionBD);
        this.clienteDAO = new ClienteDAO(conexionBD);
    }

    @Override
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pedido)
            throws PersistenciaException {

        String sp = "{CALL SP_REGISTAR_PEDIDO_PROGRAMADO(?, ?, ?, ?, ?)}";

        try (Connection conn = conexionBD.crearConexion(); CallableStatement cs = conn.prepareCall(sp)) {

            // IN 1: nota
            cs.setString(1, pedido.getNota());

            // IN 2: idCupon (nullable)
            if (pedido.getCupon() != null) {
                cs.setString(2, pedido.getCupon().getIdCupon());
            } else {
                cs.setNull(2, Types.VARCHAR);
            }

            // IN 3: idUsuario
            cs.setInt(3, pedido.getCliente().getIdUsuario());

            // IN 4: fechaHoraProgramada
            cs.setTimestamp(4, Timestamp.valueOf(pedido.getFechaHoraEntrega()));

            // OUT 5: idPedido generado
            cs.registerOutParameter(5, Types.INTEGER);

            cs.execute();

            int idGenerado = cs.getInt(5);

            if (idGenerado <= 0) {
                throw new PersistenciaException("No se pudo insertar el pedido programado");
            }

            pedido.setIdPedido(idGenerado);

            LOG.info("Pedido programado insertado con ID: " + idGenerado);

            return pedido;

        } catch (SQLException ex) {
            LOG.severe("Error al insertar pedido programado: " + ex.getMessage());
            throw new PersistenciaException("Error al insertar pedido programado", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosProgramados() throws PersistenciaException {
        List<PedidoProgramado> pedidos = new ArrayList<>();
        String sql = """
            SELECT 
                pp.idPedido,
                pp.numPedido,
                pp.nota,
                pp.idUsuario,
                pp.idCupon,
                p.estadoActual,
                p.fechaHoraPedido,
                c.nombres AS nombreCliente
            FROM PedidosProgramados pp
            INNER JOIN Pedidos p ON pp.idPedido = p.idPedido
            INNER JOIN Clientes c ON pp.idUsuario = c.idUsuario
        """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PedidoProgramado pedido = new PedidoProgramado();
                pedido.setIdPedido(rs.getInt("idPedido"));
                pedido.setNumPedido(rs.getInt("numPedido"));
                pedido.setNota(rs.getString("nota"));
                pedido.setEstadoActual(rs.getString("estadoActual"));
                pedido.setFechaHoraPedido(rs.getTimestamp("fechaHoraPedido").toLocalDateTime());

                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("idUsuario"));
                cliente.setNombres(rs.getString("nombreCliente"));
                pedido.setCliente(cliente);

                String idCupon = rs.getString("idCupon");
                if (idCupon != null) {
                    Cupon cupon = new Cupon();
                    cupon.setIdCupon(idCupon);
                    pedido.setCupon(cupon);
                }

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            LOG.severe("Error al obtener pedidos programados: " + e.getMessage());
            throw new PersistenciaException("Error al obtener pedidos programados", e);
        }

        return pedidos;
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorCliente(int idCliente) throws PersistenciaException {
        List<PedidoProgramado> pedidos = new ArrayList<>();
        String comandoSQL = """
                      SELECT
                          idPedido, 
                          numPedido, 
                          idCupon, 
                          idUsuario
                      FROM pedidosProgramados
                      WHERE idUsuario = ?;
                      """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoProgramado p = extraerPedidoProgramado(rs);
                    pedidos.add(p);
                }
            }
            return pedidos;
        } catch (SQLException ex) {
            LOG.severe("Error SQL al obtener pedidos programados: " + ex);
            throw new PersistenciaException("Error al obtener pedidos programados del cliente", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorTelefono(String telefono) throws PersistenciaException {
        List<PedidoProgramado> pedidos = new ArrayList<>();
        String comandoSQL = """
                      SELECT 
                            pp.idPedido, 
                            pp.numPedido, 
                            pp.idCupon, 
                            pp.idUsuario
                      FROM pedidosProgramados pp
                      INNER JOIN clientes cl ON pp.idUsuario = cl.idUsuario
                      INNER JOIN telefonosclientes tc ON cl.idUsuario = tc.idCliente
                      WHERE tc.telefono = ?;
                      """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PedidoProgramado p = extraerPedidoProgramado(rs);
                    pedidos.add(p);
                }
            }

            return pedidos;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al obtener pedidos programados por teléfono: " + ex);
            throw new PersistenciaException("No se pudieron recuperar los pedidos asociados al teléfono proporcionado.");
        }
    }

    /**
     * Extrae un objeto PedidoProgramado desde un ResultSet
     */
    public PedidoProgramado extraerPedidoProgramado(ResultSet rs) throws PersistenciaException {
        try {
            PedidoProgramado pedido = new PedidoProgramado();

            pedido.setIdPedido(rs.getInt("idPedido"));
            pedido.setTipo(Pedido.Tipo.PROGRAMADO);
            pedido.setNumPedido(rs.getInt("numPedido"));

            int idCliente = rs.getInt("idUsuario");
            Cliente cliente = this.clienteDAO.buscarClientePorId(idCliente);
            pedido.setCliente(cliente);

            String idCupon = rs.getString("idCupon");
            if (idCupon != null) {
                Cupon cuponExistente = this.cuponDAO.obtenerCuponPorId(idCupon);
                pedido.setCupon(cuponExistente);
            } else {
                pedido.setCupon(null);
            }

            return pedido;

        } catch (SQLException ex) {
            LOG.severe("Error al extraer el pedido programado: " + ex);
            throw new PersistenciaException("Error al extraer el pedido programado.", ex);
        }
    }

    @Override
    public double calcularTotalProgramado(int idPedido) throws PersistenciaException {
        String sql = """
        SELECT SUM(dp.cantidad * pz.precio) AS total
        FROM DetallesPedidos dp
        INNER JOIN Pizzas pz ON dp.idPizza = pz.idPizza
        WHERE dp.idPedido = ?
    """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                } else {
                    return 0.0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al calcular total del pedido programado", e);
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
