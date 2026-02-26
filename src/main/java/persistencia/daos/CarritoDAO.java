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
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Carrito;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia relacionadas
 * con la entidad {@link Carrito}.
 *
 * <p>
 * Esta clase permite crear, obtener y desactivar carritos tanto para usuarios
 * registrados como para pedidos express mediante token. Implementa el patrón
 * DAO para separar la lógica de acceso a datos de la lógica de negocio.
 * </p>
 *
 * <p>
 * Utiliza la interfaz {@link IConexionBD} para obtener conexiones a la base de
 * datos, permitiendo una arquitectura desacoplada y fácil de probar.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class CarritoDAO implements ICarritoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     *
     * Se inyecta por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger utilizado para registrar eventos importantes y errores durante
     * las operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(CarritoDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión a la base de datos.
     *
     * @param conexionBD Objeto encargado de proporcionar conexiones a la base de datos.
     */
    public CarritoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Crea un nuevo carrito en la base de datos para un usuario registrado.
     *
     * <p>
     * Se inserta un nuevo registro en la tabla Carritos con el ID del usuario,
     * la fecha actual como fecha de creación y el estado activo en verdadero.
     * </p>
     *
     * @param carrito Objeto carrito que contiene el ID del usuario.
     * @return El objeto carrito actualizado con el ID generado por la base de datos.
     * @throws PersistenciaException Si ocurre un error al insertar el carrito o al obtener el ID generado.
     */
    @Override
    public Carrito crearCarrito(Carrito carrito) throws PersistenciaException {

        String comandoSQL = """
            INSERT INTO Carritos (idUsuario, fechaCreacion, activo)
            VALUES (?, NOW(), TRUE)
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

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

    /**
     * Obtiene el carrito activo de un usuario registrado.
     *
     * <p>
     * Busca en la base de datos un carrito que pertenezca al usuario y que esté activo.
     * </p>
     *
     * @param idUsuario ID del usuario propietario del carrito.
     * @return El carrito activo si existe, o null si no se encuentra ninguno.
     * @throws PersistenciaException Si ocurre un error al consultar la base de datos.
     */
    @Override
    public Carrito obtenerCarritoActivoPorUsuario(int idUsuario) throws PersistenciaException {

        String comandoSQL = """
            SELECT idCarrito, idUsuario, fechaCreacion, activo
            FROM Carritos
            WHERE idUsuario = ? AND activo = 1
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return extraerCarrito(rs);
            }

        } catch (SQLException ex) {

            LOG.severe(() -> "Error SQL al obtener carrito activo: " + ex);
            throw new PersistenciaException("Error al obtener carrito activo.", ex);
        }
    }

    /**
     * Desactiva un carrito existente en la base de datos.
     *
     * <p>
     * Cambia el estado del carrito a inactivo (activo = false).
     * </p>
     *
     * @param idCarrito ID del carrito a desactivar.
     * @throws PersistenciaException Si no se encuentra el carrito o ocurre un error en la base de datos.
     */
    @Override
    public void desactivarCarrito(int idCarrito) throws PersistenciaException {

        String comandoSQL = """
            UPDATE Carritos
            SET activo = 0
            WHERE idCarrito = ?
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idCarrito);

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo desactivar el carrito.");
            }

        } catch (SQLException ex) {

            LOG.severe(() -> "Error SQL al desactivar carrito: " + ex);
            throw new PersistenciaException("Error al desactivar carrito.", ex);
        }
    }

    /**
     * Convierte un ResultSet en un objeto Carrito.
     *
     * @param rs ResultSet que contiene los datos del carrito.
     * @return Objeto Carrito con los datos extraídos.
     * @throws PersistenciaException Si ocurre un error al leer los datos.
     */
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

    /**
     * Crea un carrito express utilizando un token único.
     *
     * <p>
     * Este tipo de carrito es utilizado para pedidos sin usuario registrado.
     * </p>
     *
     * @param carrito Objeto carrito que contiene el token.
     * @return El carrito creado con su ID generado.
     * @throws PersistenciaException Si ocurre un error al crear el carrito.
     */
    @Override
    public Carrito crearCarritoExpress(Carrito carrito) throws PersistenciaException {

        String comandoSQL = """
            INSERT INTO Carritos (token, fechaCreacion, activo)
            VALUES (?, NOW(), TRUE)
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, carrito.getToken());

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

            LOG.info("Carrito express creado con ID: " + carrito.getIdCarrito());

            return carrito;

        } catch (SQLException ex) {

            LOG.severe("Error SQL al crear carrito express: " + ex);
            throw new PersistenciaException("Error al crear carrito express.", ex);
        }
    }

    /**
     * Obtiene el carrito express activo utilizando su token.
     *
     * @param token Token único del carrito express.
     * @return Carrito express activo o null si no existe.
     * @throws PersistenciaException Si ocurre un error al consultar la base de datos.
     */
    @Override
    public Carrito obtenerCarritoActivoExpress(String token) throws PersistenciaException {

        String comandoSQL = "SELECT idCarrito, token, fechaCreacion, activo FROM Carritos WHERE token = ? AND activo = 1";

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, token);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                return extraerCarritoExpress(rs);
            }

        } catch (SQLException ex) {

            LOG.severe("Error SQL al obtener carrito express activo: " + ex);
            throw new PersistenciaException("Error al obtener carrito express activo.", ex);
        }
    }

    /**
     * Convierte un ResultSet en un objeto Carrito express.
     *
     * @param rs ResultSet con los datos del carrito express.
     * @return Objeto Carrito express.
     * @throws PersistenciaException Si ocurre un error al leer los datos.
     */
    private Carrito extraerCarritoExpress(ResultSet rs) throws PersistenciaException {

        try {

            Carrito carrito = new Carrito();

            carrito.setIdCarrito(rs.getInt("idCarrito"));
            carrito.setToken(rs.getString("token"));

            Timestamp timestamp = rs.getTimestamp("fechaCreacion");

            if (timestamp != null) {
                carrito.setFechaCreacion(timestamp.toLocalDateTime());
            }

            carrito.setActivo(rs.getBoolean("activo"));

            return carrito;

        } catch (SQLException ex) {

            throw new PersistenciaException("Error al extraer carrito express.", ex);
        }
    }
}