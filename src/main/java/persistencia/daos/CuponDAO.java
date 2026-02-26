/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.CuponDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia de la entidad {@link Cupon}.
 *
 * <p>
 * Esta clase permite consultar, validar y actualizar la información de los cupones
 * almacenados en la base de datos. Implementa el patrón DAO (Data Access Object),
 * separando la lógica de acceso a datos de la lógica de negocio.
 * </p>
 *
 * <p>
 * Entre sus funcionalidades principales se encuentran:
 * </p>
 * <ul>
 *   <li>Obtener un cupón por su identificador</li>
 *   <li>Incrementar la cantidad de usos de un cupón</li>
 *   <li>Validar la vigencia y disponibilidad de un cupón</li>
 * </ul>
 *
 * <p>
 * Utiliza la interfaz {@link IConexionBD} para gestionar las conexiones a la base de datos,
 * permitiendo una arquitectura desacoplada y fácil de mantener.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class CuponDAO implements ICuponDAO {

    /**
     * Componente encargado de crear conexiones con la base de datos.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger utilizado para registrar eventos importantes y errores durante
     * las operaciones de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(CuponDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión a la base de datos.
     *
     * @param conexionBD Objeto encargado de crear conexiones a la base de datos.
     */
    public CuponDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Obtiene un cupón de la base de datos utilizando su identificador.
     *
     * <p>
     * Consulta la información del cupón, incluyendo su vigencia, cantidad de usos
     * y porcentaje de descuento.
     * </p>
     *
     * @param id Identificador único del cupón.
     * @return Objeto Cupon con la información obtenida.
     * @throws PersistenciaException Si el cupón no existe o ocurre un error en la base de datos.
     */
    @Override
    public Cupon obtenerCuponPorId(String id) throws PersistenciaException {

        String comandoSQL = """
                            SELECT 
                                idCupon, 
                                vigencia, 
                                cantUsos, 
                                porcentajeDescuento
                            FROM cupones
                            WHERE idCupon = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    LOG.log(Level.WARNING, "No se encontró el cupón con id {0}", id);
                    throw new PersistenciaException("Cupón inexistente.");
                }

                return extraerCupon(rs);
            }

        } catch (SQLException ex) {

            LOG.severe("Error al obtener cupón: " + ex);
            throw new PersistenciaException("Error al acceder a la base de datos.", ex);
        }
    }

    /**
     * Incrementa la cantidad de usos de un cupón en la base de datos.
     *
     * <p>
     * Actualiza el campo cantUsos sumando una unidad.
     * </p>
     *
     * @param cupon Objeto Cupon cuyo contador de usos será incrementado.
     * @return Objeto Cupon actualizado.
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public Cupon incrementarUsosCupon(Cupon cupon) throws PersistenciaException {

        String comandoSQL = """
                            UPDATE cupones
                            SET cantUsos = cantUsos + 1
                            WHERE idCupon = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, cupon.getIdCupon());

            if (ps.executeUpdate() == 0) {

                LOG.warning("No se pudo incrementar el cupón: " + cupon.getIdCupon());
                throw new PersistenciaException("No fue posible utilizar el cupón.");
            }

            cupon.setCantidadUsos(cupon.getCantidadUsos() + 1);

            return cupon;

        } catch (SQLException ex) {

            LOG.severe("Error al incrementar usos del cupón: " + ex);
            throw new PersistenciaException("Error al utilizar el cupón.", ex);
        }
    }

    /**
     * Construye un objeto Cupon a partir de un ResultSet.
     *
     * <p>
     * Extrae los datos del cupón desde el ResultSet y los asigna al objeto Cupon.
     * </p>
     *
     * @param rs ResultSet con los datos del cupón.
     * @return Objeto Cupon con la información extraída.
     * @throws PersistenciaException Si ocurre un error al leer los datos.
     */
    public Cupon extraerCupon(ResultSet rs) throws PersistenciaException {

        try {

            Cupon cupon = new Cupon();

            cupon.setIdCupon(rs.getString("idCupon"));
            cupon.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));

            Timestamp ts = rs.getTimestamp("vigencia");

            if (ts != null) {
                cupon.setVigencia(ts.toLocalDateTime());
            }

            cupon.setCantidadUsos(rs.getInt("cantUsos"));

            return cupon;

        } catch (SQLException ex) {

            LOG.severe("Error al extraer cupón: " + ex);
            throw new PersistenciaException("Error al extraer el cupón.", ex);
        }
    }

    /**
     * Valida un cupón mediante un procedimiento almacenado.
     *
     * <p>
     * Verifica si el cupón existe, está vigente y puede ser utilizado.
     * </p>
     *
     * @param codigo Código del cupón.
     * @return Objeto Cupon válido, o null si el cupón no es válido.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    @Override
    public Cupon validarCupon(String codigo) throws PersistenciaException {

        String sql = "CALL SP_VALIDAR_CUPON(?)";

        try (Connection con = conexionBD.crearConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, codigo);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {

                Cupon cupon = new Cupon();

                cupon.setIdCupon(rs.getString("idCupon"));
                cupon.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
                cupon.setCantidadUsos(rs.getInt("cantUsos"));
                cupon.setVigencia(rs.getTimestamp("vigencia").toLocalDateTime());

                return cupon;
            }

            return null;

        } catch (SQLException e) {

            throw new PersistenciaException("Error al validar el cupón.", e);
        }
    }
}