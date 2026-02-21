/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package persistencia.DAOS;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
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
 * Implementación del DAO para la entidad Cliente
 *
 * <p>
 * Esta clase se encarga de toda la comunicación con la base de datos para la
 * entidad Cliente.</p>
 *
 * <p>
 * Incluye las operaciones basicas CRUD(Create, Read, Update, Delete) que toma
 * de la tabla Clientes de la BD</p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class ClienteDAO implements IClienteDAO {

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
    public ClienteDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     * 
     * Se ejecuta una sentencia INSERT utilizando la información contenida
     * en el objeto Cliente. Una vez realizado el registro, se obtiene el
     * identificador generado automáticamente por la base de datos y se
     * asigna al objeto.
     * 
     * El objeto Cliente debe contener todos los datos obligatorios
     * requeridos para su almacenamiento.
     * 
     * @param cliente objeto Cliente con la información necesaria para su registro
     * @return objeto Cliente con el identificador generado asignado
     * @throws PersistenciaException si ocurre un error durante la inserción
     * o si no es posible obtener el identificador generado
     */
    @Override
    public Cliente agregarCliente(Cliente cliente) throws PersistenciaException {
        String comandoSQL = """
                            INSERT INTO clientes
                            	(nombres, apellidoPaterno, apellidoMaterno, fechaNacimiento, idDomicilioCliente)
                            VALUES
                            	(?, ?, ?, ?, ?);
                            """;
        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, cliente.getNombres());
            ps.setString(2, cliente.getApellidoPaterno());

            if (cliente.getApellidoMaterno() != null) {
                ps.setString(3, cliente.getApellidoMaterno());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }

            ps.setDate(4, Date.valueOf(cliente.getFechaNacimiento()));
            ps.setInt(5, cliente.getDomicilio().getIdDomicilio());

            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                LOG.warning("No se pudo insertar al cliente: " + cliente);
                throw new PersistenciaException("No se logró agregar al cliente.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setIdCliente(rs.getInt(1));
                } else {
                    throw new PersistenciaException("Error al obtener el ID generado del nuevo cliente.");
                }
            }

            LOG.info("Cliente insertado con éxito. ID: " + cliente.getIdCliente());
            return cliente;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al insertar al cliente" + ex);
            throw new PersistenciaException("Error al insertar al cliente en la base de datos", ex);
        }
    }

    /**
     * Actualiza la información de un cliente existente en la base de datos.
     * 
     * Se ejecuta una sentencia UPDATE utilizando el identificador del cliente
     * para modificar sus datos personales y su domicilio asociado.
     * 
     * El objeto Cliente debe contener un id válido previamente registrado
     * en la base de datos.
     * 
     * @param cliente objeto Cliente con el identificador y los nuevos
     * valores a actualizar
     * @return objeto Cliente con la información actualizada
     * @throws PersistenciaException si el cliente no existe o si ocurre
     * un error durante la actualización en la base de datos
     */
    @Override
    public Cliente actualizarCliente(Cliente cliente) throws PersistenciaException {
        String comandoSQL = """
                            UPDATE clientes
                            SET nombres=?,
                            	apellidoPaterno = ?,
                                apellidoMaterno = ?,
                                fechaNacimiento = ?,
                                idDomicilioCliente = ?
                            WHERE idUsuario = ?;
                            """;
        
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            
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
            
            if(ps.executeUpdate() == 0){
                LOG.warning("No se pudo actualizar al cliente. Cliente: "+cliente.toString());
                throw new PersistenciaException("No fue posible actualizar al cliente: "+cliente.toString());
            }
            
            return cliente;
            
        } catch (SQLException ex) {
            LOG.severe("Error SQL al actualizar al tecnico");
            throw new PersistenciaException("Error al actualizar al cliente en la base de datos", ex);
        }
    }
    
     /**
     * Busca un cliente en la base de datos mediante su identificador.
     * 
     * Se ejecuta una consulta SQL utilizando el id proporcionado
     * para recuperar la información correspondiente al cliente.
     * 
     * @param id identificador único del cliente a buscar
     * @return objeto Cliente encontrado en la base de datos
     * @throws PersistenciaException si no existe un cliente con el id
     * proporcionado o si ocurre un error durante la consulta
     */
    @Override
    public Cliente buscarClientePorId(int id) throws PersistenciaException {
        String comandoSQL = """
                            SELECT
                            	idUsuario, nombres, apellidoPaterno, apellidoMaterno, fechaNacimiento, idDomicilioCliente
                            FROM clientes
                            WHERE idUsuario = ?
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    LOG.log(Level.WARNING, "No se encontró el Cliente con id {0}", id);
                    throw new PersistenciaException("No existe el cliente con el ID proporcionado.");
                }

                return extraerCliente(rs);
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al buscar cliente por teléfono: " + ex);
            throw new PersistenciaException("Error al encontrar  al cliente en la base de datos", ex);
        }
    }
    
    @Override
    public Cliente buscarClientePorTelefono(String telefono) throws PersistenciaException {
        String comandoSQL = """
                            SELECT
                            	idUsuario, 
                                nombres, 
                                apellidoPaterno, 
                                apellidoMaterno, 
                                fechaNacimiento, 
                                idDomicilioCliente
                            FROM clientes as cl
                            INNER JOIN telefonosclientes as tel on tel.idCliente = cl.idUsuario
                            WHERE tel.telefono = ?;
                            """;

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {

            ps.setString(1, telefono);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    LOG.log(Level.WARNING, "No se encontró el Cliente con id {0}", telefono);
                    throw new PersistenciaException("No existe el cliente con el ID proporcionado.");
                }

                return extraerCliente(rs);
            }

        } catch (SQLException ex) {
            LOG.severe("Error SQL al buscar cliente por teléfono: " + ex);
            throw new PersistenciaException("Error al encontrar al cliente en la base de datos", ex);
        }
    }

     /**
     * Construye un objeto Cliente a partir de un ResultSet.
     * 
     * Extrae los valores correspondientes a cada atributo del cliente
     * desde el registro actual del ResultSet y los asigna al objeto.
     * 
     * @param rs ResultSet posicionado en un registro válido
     * @return objeto Cliente con la información extraída
     * @throws PersistenciaException si ocurre un error al obtener
     * los datos del ResultSet
     */
    public Cliente extraerCliente(ResultSet rs) throws PersistenciaException {
        try {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(rs.getInt("idUsuario"));
            cliente.setNombres(rs.getString("nombres"));
            cliente.setApellidoPaterno(rs.getString("apellidoPaterno"));
            cliente.setApellidoMaterno(rs.getString("apellidoMaterno"));

            Date fechaNacimiento = rs.getDate("fechaNacimiento");

            if (fechaNacimiento != null) {
                cliente.setFechaNacimiento(fechaNacimiento.toLocalDate());
            } else {
                cliente.setFechaNacimiento(null);
            }

            return cliente;
        } catch (SQLException ex) {
            System.getLogger(ClienteDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            throw new PersistenciaException("Error al extaer el cliente.");
        }
    }

    /**
     * Consulta todos los clientes registrados en la base de datos.
     * 
     * Se realiza una consulta general sin filtros y por cada registro
     * obtenido se construye un objeto Cliente que es agregado a una lista.
     * 
     * 
     * @return lista con todos los clientes registrados en la base de datos
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    @Override
    public List<Cliente> consultarClientes() throws PersistenciaException {
        String comandoSQL = """
                            SELECT 
                            	idUsuario, 
                                nombres, 
                                apellidoPaterno, 
                                apellidoMaterno, 
                                fechaNacimiento, 
                                idDomicilioCliente
                            FROM clientes
                            """;
        
        List<Cliente> listaClientes = new ArrayList<>();

        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)) {
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                Cliente cliente = extraerCliente(rs);
                
                int idDomicilio = rs.getInt("idDomicilioCliente");
                Domicilio domicilio = new Domicilio();
                domicilio.setIdDomicilio(idDomicilio);
                cliente.setDomicilio(domicilio);
                
                listaClientes.add(cliente);
            }
            
            return listaClientes;

        } catch (SQLException ex) {
            LOG.severe("Error SQL al consultar clientes");
            throw new PersistenciaException("Error al consultar clientes", ex);
        }
        
    }

}