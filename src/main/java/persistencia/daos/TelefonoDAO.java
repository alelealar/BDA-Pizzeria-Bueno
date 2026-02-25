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
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;
import persistencia.DAOS.IClienteDAO;
import persistencia.dominio.Cliente;




/**
 *
 * @author Alejandra Leal Armenta, 262719
 */


public class TelefonoDAO implements ITelefonoDAO{
    /**
     * Componente encargado de crear conexiones con la base de datos.
     * 
     * Se inyecta por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final IConexionBD conexionBD;
    
    private final IClienteDAO clienteDAO;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(TelefonoDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public TelefonoDAO(IConexionBD conexionBD, IClienteDAO clienteDAO) {
        this.conexionBD = conexionBD;
        this.clienteDAO = clienteDAO;
    }

    @Override
    public Telefono agregarTelefono(Telefono telefono) throws PersistenciaException{
        String comandoSQL = """
                            INSERT INTO telefonosclientes
                            	(telefono, etiqueta, idCliente)
                            VALUES
                            	(?, ?, ?)
                            """;
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, telefono.getTelefono());
            ps.setString(2, telefono.getEtiqueta());
            ps.setInt(3, telefono.getCliente().getIdUsuario());
            
            int filasInsertadas = ps.executeUpdate();

            if (filasInsertadas == 0) {
                LOG.warning("No se logro insertar el telefono "+telefono+" al cliente con id {0}");
                throw new PersistenciaException("No se insertó el teléfono");
            }
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);

                    Cliente cliente = clienteDAO.buscarClientePorId(telefono.getCliente().getIdUsuario());

                    Telefono tel = new Telefono();
                    tel.setIdTelefono(idGenerado);
                    tel.setTelefono(telefono.getTelefono());
                    tel.setEtiqueta(telefono.getEtiqueta());
                    tel.setCliente(cliente);

                    LOG.info("El telefono fie agregado");
                    return tel;
                }
                throw new PersistenciaException("No se pudo obtener el ID generado");   
            }            
        } catch (SQLException ex) {
            LOG.severe("Error al agregar telefono a la BD");
            throw new PersistenciaException("No se logro agregar el telefono ingresado");
        }
    }

    @Override
    public Telefono eliminarTelefono(int idTelefono) throws PersistenciaException {
        String comandoSQL = """
                            DELETE FROM telefonosclientes
                            WHERE idCliente = ? AND telefono = ?;
                            """;
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            
            Telefono telefono = obtenerTelefono(idTelefono);
            ps.setInt(1, idTelefono);
            ps.setString(2, telefono.getTelefono());
            
            int filasEliminadas = ps.executeUpdate();
            
            if(filasEliminadas < 1){
                LOG.warning("No se logro eliminar el telefono");
                throw new PersistenciaException("No fue posible eliminar el telefono");
            }
            
            return telefono;
            
            
        } catch (SQLException ex) {
            LOG.severe("Error SQL al eliminar el telefono");
            throw new PersistenciaException("Hubo un error al eliminar el telefono. Intentelo mas tarde.");
        }
    }
    
    
    public Telefono obtenerTelefono(int idTelefono) throws PersistenciaException{
        String comandoSQL = """
                            SELECT 
                            	idTelefono, 
                                telefono,
                                etiqueta, 
                                idCliente
                            FROM telefonosclientes
                            WHERE idTelefono = ?;
                            """;
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            ps.setInt(1, idTelefono);
            
            try(ResultSet rs = ps.executeQuery()){
                if (!rs.next()) {
                    LOG.log(Level.WARNING, "No se encontró el telefono con id {0}");
                    throw new PersistenciaException("No existe el telefono con el ID proporcionado.");
                }
                Telefono tel = new Telefono();
                tel.setIdTelefono(rs.getInt("idTelefono"));
                tel.setTelefono(rs.getString("telefono"));
                tel.setEtiqueta(rs.getString("etiqueta"));
                int idCliente = rs.getInt("idCliente");
                
                tel.setCliente(clienteDAO.buscarClientePorId(idCliente));
                
                return tel;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TelefonoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new PersistenciaException("Error para obtener el telefono");
        }
    }

}
