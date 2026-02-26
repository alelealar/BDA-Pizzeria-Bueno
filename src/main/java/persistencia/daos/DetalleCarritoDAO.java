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
import persistencia.conexion.IConexionBD;
import persistencia.dominio.DetalleCarrito;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia de los
 * detalles de un carrito de compras.
 *
 * <p>
 * Esta clase permite agregar, consultar, actualizar y eliminar los productos
 * asociados a un carrito en la base de datos. Implementa el patrón DAO
 * (Data Access Object), permitiendo separar la lógica de acceso a datos de la
 * lógica de negocio.
 * </p>
 *
 * <p>
 * Cada registro representa un producto agregado al carrito, incluyendo
 * información como el identificador del carrito, la pizza, el tamaño,
 * la cantidad y cualquier nota adicional.
 * </p>
 *
 * Utiliza la interfaz {@link IConexionBD} para gestionar las conexiones
 * a la base de datos de forma desacoplada.
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class DetalleCarritoDAO implements IDetalleCarritoDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger utilizado para registrar eventos y errores durante las operaciones
     * de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(DetalleCarritoDAO.class.getName());

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @param conexionBD Objeto encargado de crear conexiones a la base de datos.
     */
    public DetalleCarritoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Agrega un nuevo producto al carrito en la base de datos.
     *
     * <p>
     * Inserta un registro en la tabla DetallesCarrito con la información
     * proporcionada en el objeto DetalleCarrito.
     * </p>
     *
     * @param detalle Objeto que contiene la información del producto a agregar.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public void agregarProducto(DetalleCarrito detalle) throws PersistenciaException {

        String comandoSQL = """
            INSERT INTO DetallesCarrito (idCarrito, idPizza, tamaño, cantidad, nota)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, detalle.getIdCarrito());
            ps.setInt(2, detalle.getIdPizza());
            ps.setString(3, detalle.getTamanio());
            ps.setInt(4, detalle.getCantidad());
            ps.setString(5, detalle.getNota());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo agregar el producto al carrito.");
            }

        } catch (SQLException ex) {

            LOG.severe("Error SQL al agregar producto al carrito: " + ex);
            throw new PersistenciaException("Error al agregar producto al carrito.", ex);
        }
    }

    /**
     * Obtiene un detalle específico del carrito según el carrito, pizza y tamaño.
     *
     * @param idCarrito Identificador del carrito.
     * @param idPizza Identificador de la pizza.
     * @param tamanio Tamaño de la pizza.
     * @return Objeto DetalleCarrito encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio)
            throws PersistenciaException {

        String comandoSQL = """
            SELECT idDetalleCarrito, idCarrito, idPizza, tamaño, cantidad, nota
            FROM DetallesCarrito
            WHERE idCarrito = ? AND idPizza = ? AND tamaño = ?
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

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

    /**
     * Elimina un detalle del carrito de la base de datos.
     *
     * @param idDetalleCarrito Identificador del detalle a eliminar.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    @Override
    public void eliminarDetalleCarrito(int idDetalleCarrito)
            throws PersistenciaException {

        String comandoSQL = """
            DELETE FROM DetallesCarrito
            WHERE idDetalleCarrito = ?
            """;

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, idDetalleCarrito);
            ps.executeUpdate();

        } catch (SQLException ex) {

            LOG.severe("Error SQL al eliminar detalles del carrito: " + ex);
            throw new PersistenciaException("Error al eliminar detalles del carrito.", ex);
        }
    }

    /**
     * Construye un objeto DetalleCarrito a partir de un ResultSet.
     *
     * @param rs ResultSet con los datos del detalle del carrito.
     * @return Objeto DetalleCarrito con la información extraída.
     * @throws PersistenciaException Si ocurre un error al leer los datos.
     */
    private DetalleCarrito extraerDetalle(ResultSet rs)
            throws PersistenciaException {

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

            throw new PersistenciaException("Error al extraer detalle del carrito.", ex);
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito.
     *
     * @param idDetalle Identificador del detalle.
     * @param nuevaCantidad Nueva cantidad del producto.
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public void actualizarCantidad(int idDetalle, int nuevaCantidad)
            throws PersistenciaException {

        String sql = """
            UPDATE DetallesCarrito
            SET cantidad = ?
            WHERE idDetalleCarrito = ?
            """;

        try (Connection con = conexionBD.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idDetalle);
            ps.executeUpdate();

        } catch (SQLException e) {

            throw new PersistenciaException("Error al actualizar cantidad.", e);
        }
    }

    /**
     * Obtiene todos los detalles asociados a un carrito específico.
     *
     * @param idCarrito Identificador del carrito.
     * @return Lista de objetos DetalleCarrito.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito)
            throws PersistenciaException {

        String comandoSQL = """
            SELECT idDetalleCarrito, idCarrito, idPizza, tamaño, cantidad, nota
            FROM DetallesCarrito
            WHERE idCarrito = ?
            """;

        List<DetalleCarrito> lista = new ArrayList<>();

        try (Connection conn = conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

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