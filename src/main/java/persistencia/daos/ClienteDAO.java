/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.DAOS;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cliente;
import persistencia.dominio.Domicilio;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase DAO encargada de gestionar las operaciones de persistencia de la entidad {@link Cliente}.
 *
 * <p>
 * Esta clase implementa el patrón DAO (Data Access Object), permitiendo la
 * comunicación entre la aplicación y la base de datos para realizar operaciones
 * relacionadas con los clientes.
 * </p>
 *
 * <p>
 * Incluye operaciones como:
 * </p>
 * <ul>
 *   <li>Registrar un cliente</li>
 *   <li>Actualizar información de un cliente</li>
 *   <li>Buscar cliente por ID o teléfono</li>
 *   <li>Consultar todos los clientes</li>
 *   <li>Registrar teléfonos asociados a un cliente</li>
 * </ul>
 *
 * <p>
 * Utiliza la interfaz {@link IConexionBD} para obtener conexiones a la base de datos,
 * permitiendo desacoplamiento y facilidad de pruebas.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class ClienteDAO implements IClienteDAO {

    /**
     * Componente encargado de proporcionar conexiones a la base de datos.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger utilizado para registrar eventos y errores durante las operaciones
     * de persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que inicializa la conexión a la base de datos.
     *
     * @param conexionBD Objeto encargado de crear conexiones a la base de datos.
     */
    public ClienteDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Registra un nuevo cliente en la base de datos utilizando un procedimiento almacenado.
     *
     * <p>
     * El procedimiento almacenado inserta los datos del cliente y devuelve el ID generado.
     * Este ID es asignado al objeto cliente.
     * </p>
     *
     * @param cliente Objeto Cliente con la información a registrar.
     * @return Objeto Cliente con el ID generado.
     * @throws PersistenciaException Si ocurre un error durante el registro.
     */
    @Override
    public Cliente agregarCliente(Cliente cliente) throws PersistenciaException {

        String procedimientoSQL = "{CALL SP_REGISTRAR_CLIENTE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = this.conexionBD.crearConexion();
             CallableStatement cs = conn.prepareCall(procedimientoSQL)) {

            cs.setString(1, cliente.getNombreUsuario());
            cs.setString(2, cliente.getContraseniaUsuario());
            cs.setString(3, cliente.getNombres());
            cs.setString(4, cliente.getApellidoPaterno());

            if (cliente.getApellidoMaterno() != null) {
                cs.setString(5, cliente.getApellidoMaterno());
            } else {
                cs.setNull(5, Types.VARCHAR);
            }

            cs.setDate(6, Date.valueOf(cliente.getFechaNacimiento()));
            cs.setString(7, cliente.getDomicilio().getCalle());
            cs.setString(8, cliente.getDomicilio().getNumero());
            cs.setString(9, cliente.getDomicilio().getColonia());
            cs.setString(10, cliente.getDomicilio().getCodigoPostal());

            cs.registerOutParameter(11, Types.INTEGER);

            cs.execute();

            int idGenerado = cs.getInt(11);

            if (idGenerado == -1) {
                throw new SQLException("Error interno en el procedimiento almacenado.");
            }

            cliente.setIdCliente(idGenerado);

            LOG.info("Cliente registrado con éxito. ID: " + idGenerado);

            return cliente;

        } catch (SQLException ex) {

            LOG.severe("Error SQL al registrar cliente: " + ex);
            throw new PersistenciaException("No se pudo registrar el cliente.", ex);
        }
    }

    /**
     * Registra una lista de teléfonos asociados a un cliente.
     *
     * <p>
     * Utiliza ejecución por lotes (batch) para mejorar el rendimiento.
     * </p>
     *
     * @param idCliente ID del cliente.
     * @param telefonos Lista de teléfonos a registrar.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public void agregarTelefonos(int idCliente, List<Telefono> telefonos) throws PersistenciaException {

        String comandoSQL = """
                            INSERT INTO telefonosclientes
                            (telefono, etiqueta, idCliente)
                            VALUES (?, ?, ?)
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            for (Telefono tel : telefonos) {

                ps.setString(1, tel.getTelefono());
                ps.setString(2, tel.getEtiqueta());
                ps.setInt(3, idCliente);

                ps.addBatch();
            }

            ps.executeBatch();

            LOG.info("Se registraron " + telefonos.size() + " teléfonos.");

        } catch (SQLException ex) {

            LOG.severe("Error SQL al registrar teléfonos: " + ex);
            throw new PersistenciaException("No se pudieron registrar los teléfonos.", ex);
        }
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param cliente Cliente con los nuevos datos.
     * @return Cliente actualizado.
     * @throws PersistenciaException Si ocurre un error o el cliente no existe.
     */
    @Override
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException {

        String comandoSQL = """
                            UPDATE clientes
                            SET nombres = ?,
                                apellidoPaterno = ?,
                                apellidoMaterno = ?,
                                fechaNacimiento = ?,
                                idDomicilioCliente = ?
                            WHERE idUsuario = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidoPaterno());

            if (cliente.getApellidoMaterno() != null) {
                ps.setString(3, cliente.getApellidoMaterno());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            ps.setDate(4, Date.valueOf(cliente.getFechaNacimiento()));
            ps.setInt(5, cliente.getDomicilio().getIdDomicilio());
            ps.setInt(6, cliente.getIdCliente());

            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("No se pudo actualizar el cliente.");
            }

            return cliente;

        } catch (SQLException ex) {

            LOG.severe("Error SQL al actualizar cliente.");
            throw new PersistenciaException("Error al actualizar cliente.", ex);
        }
    }

    /**
     * Busca un cliente por su ID.
     *
     * @param id ID del cliente.
     * @return Cliente encontrado.
     * @throws PersistenciaException Si no existe o ocurre un error.
     */
    @Override
    public Cliente buscarClientePorId(int id) throws PersistenciaException {

        String comandoSQL = """
                            SELECT idUsuario, nombres, apellidoPaterno,
                                   apellidoMaterno, fechaNacimiento,
                                   idDomicilioCliente
                            FROM clientes
                            WHERE idUsuario = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new PersistenciaException("Cliente no encontrado.");
            }

            return extraerCliente(rs);

        } catch (SQLException ex) {

            throw new PersistenciaException("Error al buscar cliente.", ex);
        }
    }

    /**
     * Busca un cliente utilizando su número de teléfono.
     *
     * @param telefono Número telefónico.
     * @return Cliente encontrado.
     * @throws PersistenciaException Si no existe o ocurre un error.
     */
    @Override
    public Cliente buscarClientePorTelefono(String telefono) throws PersistenciaException {

        String comandoSQL = """
                            SELECT idUsuario, nombres, apellidoPaterno,
                                   apellidoMaterno, fechaNacimiento,
                                   idDomicilioCliente
                            FROM clientes cl
                            INNER JOIN telefonosclientes tel
                            ON tel.idCliente = cl.idUsuario
                            WHERE tel.telefono = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, telefono);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new PersistenciaException("Cliente no encontrado.");
            }

            return extraerCliente(rs);

        } catch (SQLException ex) {

            throw new PersistenciaException("Error al buscar cliente.", ex);
        }
    }

    /**
     * Construye un objeto Cliente a partir de un ResultSet.
     *
     * @param rs ResultSet con los datos.
     * @return Cliente construido.
     * @throws PersistenciaException Si ocurre un error.
     */
    public Cliente extraerCliente(ResultSet rs) throws PersistenciaException {

        try {

            Cliente cliente = new Cliente();

            cliente.setIdCliente(rs.getInt("idUsuario"));
            cliente.setNombres(rs.getString("nombres"));
            cliente.setApellidoPaterno(rs.getString("apellidoPaterno"));
            cliente.setApellidoMaterno(rs.getString("apellidoMaterno"));

            Date fecha = rs.getDate("fechaNacimiento");

            if (fecha != null) {
                cliente.setFechaNacimiento(fecha.toLocalDate());
            }

            return cliente;

        } catch (SQLException ex) {

            throw new PersistenciaException("Error al extraer cliente.", ex);
        }
    }

    /**
     * Consulta todos los clientes registrados.
     *
     * @return Lista de clientes.
     * @throws PersistenciaException Si ocurre un error.
     */
    @Override
    public List<Cliente> consultarClientes() throws PersistenciaException {

        String comandoSQL = """
                            SELECT idUsuario, nombres, apellidoPaterno,
                                   apellidoMaterno, fechaNacimiento,
                                   idDomicilioCliente
                            FROM clientes
                            """;

        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = this.conexionBD.crearConexion();
             PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Cliente cliente = extraerCliente(rs);

                Domicilio domicilio = new Domicilio();
                domicilio.setIdDomicilio(rs.getInt("idDomicilioCliente"));

                cliente.setDomicilio(domicilio);

                lista.add(cliente);
            }

            return lista;

        } catch (SQLException ex) {

            throw new PersistenciaException("Error al consultar clientes.", ex);
        }
    }
}