/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Carrito;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class CarritoDAO implements ICarritoDAO {

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
    public CarritoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public Carrito crearCarrito(Carrito carrito) throws PersistenciaException {

        String comandoSQL = """
            INSERT INTO Carritos (idUsuario, fechaCreacion, activo)
            VALUES (?, NOW(), TRUE);
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, carrito.getIdUsuario());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new PersistenciaException("No se pudo crear el carrito.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    carrito.setIdCarrito(rs.getInt(1));
                } else {
                    throw new PersistenciaException("No se pudo obtener el ID del carrito.");
                }
            }

            LOG.info("Carrito creado con ID: " + carrito.getIdCarrito());
            return carrito;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al crear carrito: " + ex);
            throw new PersistenciaException("Error al crear carrito.", ex);
        }
    }

    @Override
    public Carrito obtenerCarritoActivoPorUsuario(int idUsuario) throws PersistenciaException {

        String comandoSQL = """
            SELECT idCarrito, idUsuario, fechaCreacion, activo
            FROM Carritos
            WHERE idUsuario = ? AND activo = TRUE;
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return extraerCarrito(rs);
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al obtener carrito activo: " + ex);
            throw new PersistenciaException("Error al obtener carrito activo.", ex);
        }
    }

    @Override
    public void desactivarCarrito(int idCarrito) throws PersistenciaException {

        String comandoSQL = """
            UPDATE Carritos
            SET activo = FALSE
            WHERE idCarrito = ?;
            """;

        try (Connection conn = conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCarrito);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo desactivar el carrito.");
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al desactivar carrito: " + ex);
            throw new PersistenciaException("Error al desactivar carrito.", ex);
        }
    }
    
     private Carrito extraerCarrito(ResultSet rs) throws PersistenciaException {
        try {
            Carrito carrito = new Carrito();
            carrito.setIdCarrito(rs.getInt("idCarrito"));
            carrito.setIdUsuario(rs.getInt("idUsuario"));

            Timestamp timestamp = rs.getTimestamp("fechaCreacion");
            if (timestamp != null) {
                carrito.setFechaCreacion(timestamp.toLocalDateTime());
            }

            carrito.setActivo(rs.getBoolean("activo"));

            return carrito;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al extraer carrito.", ex);
        }
    }
}