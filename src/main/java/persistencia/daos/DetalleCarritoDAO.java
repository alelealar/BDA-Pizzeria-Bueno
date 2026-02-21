/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.DetalleCarrito;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class DetalleCarritoDAO implements IDetalleCarritoDAO {

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
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public DetalleCarritoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public void agregarProducto(DetalleCarrito detalle) throws PersistenciaException {

        String comandoSQL = """
            INSERT INTO DetallesCarrito (idCarrito, idPizza, cantidad, nota)
            VALUES (?, ?, ?, ?);
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, detalle.getIdCarrito());
            ps.setInt(2, detalle.getIdPizza());
            ps.setInt(3, detalle.getCantidad());
            ps.setString(4, detalle.getNota());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo agregar el producto al carrito.");
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al agregar producto al carrito: " + ex);
            throw new PersistenciaException("Error al agregar producto al carrito.", ex);
        }
    }

    @Override
    public DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio) throws PersistenciaException {

        String comandoSQL = """
        SELECT idDetalleCarrito, idCarrito, idPizza, tamaño, cantidad, nota
        FROM DetallesCarrito
        WHERE idCarrito = ? AND idPizza = ? AND tamanio = ?;
        """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCarrito);
            ps.setInt(2, idPizza);
            ps.setString(3, tamanio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extraerDetalle(rs);
                }
            }

            return null;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al obtener detalle específico: " + ex);
            throw new PersistenciaException("Error al obtener detalle específico.", ex);
        }
    }

    @Override
    public void eliminarDetallesPorCarrito(int idCarrito) throws PersistenciaException {

        String comandoSQL = """
            DELETE FROM DetallesCarrito
            WHERE idDetalleCarrito = ?;
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCarrito);
            ps.executeUpdate();

        } catch (SQLException ex) {
            LOG.severe("Error SQL al eliminar detalles del carrito: " + ex);
            throw new PersistenciaException("Error al eliminar detalles del carrito.", ex);
        }
    }

    private DetalleCarrito extraerDetalle(ResultSet rs) throws PersistenciaException {
        try {
            DetalleCarrito detalle = new DetalleCarrito();
            detalle.setIdDetalleCarrito(rs.getInt("idDetalleCarrito"));
            detalle.setIdCarrito(rs.getInt("idCarrito"));
            detalle.setIdPizza(rs.getInt("idPizza"));
            detalle.setCantidad(rs.getInt("cantidad"));
            detalle.setTamanio(rs.getString("tamaño"));
            detalle.setNota(rs.getString("nota"));
            return detalle;
        } catch (SQLException ex) {
            throw new PersistenciaException("Error al extraer detalle carrito.", ex);
        }
    }

    @Override
    public void actualizarCantidad(int idDetalle,
            int nuevaCantidad)
            throws PersistenciaException {

        String sql = """
        UPDATE DetallesCarritos
        SET cantidad = ?
        WHERE idDetalleCarrito = ?
        """;

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idDetalle);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar cantidad", e);
        }
    }

    @Override
    public List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito) throws PersistenciaException {

        String comandoSQL = """
        SELECT idDetalleCarrito, idCarrito, idPizza, tamanio, cantidad, nota
        FROM DetallesCarrito
        WHERE idCarrito = ?;
        """;

        List<DetalleCarrito> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCarrito);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(extraerDetalle(rs));
                }
            }

            return lista;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al consultar detalles del carrito: " + ex);
            throw new PersistenciaException("Error al consultar detalles del carrito.", ex);
        }
    }
}