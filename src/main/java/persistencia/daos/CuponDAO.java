/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class CuponDAO implements ICuponDAO{
    
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
    public CuponDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    @Override
    public Cupon obtenerCuponPorId(String id) throws PersistenciaException {
        String comandoSQL = """
                            SELECT 
                            	idCupon, 
                                vigencia, 
                                cantUsos, 
                                porcentajeDescuento
                            FROM cupones
                            WHERE idCupon = ?;
                            """;
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            ps.setString(1, id);
            
            try (ResultSet rs = ps.executeQuery()){
                if(!rs.next()){
                    LOG.log(Level.WARNING, "No se encontró el cupon con id {0}", id);
                    throw new PersistenciaException("Cupón inexistente");
                }
                
                return extraerCupon(rs);        
            }     
        } catch (SQLException ex) {
            LOG.severe("Hubo un error al acceder a la base de datos. "+ex);
            throw new PersistenciaException("Hubo un error al acceder a la base de datos.", ex);
        }
    }

    @Override
    public Cupon incrementarUsosCupon(Cupon cupon) throws PersistenciaException {
        String comandoSQL = """
                            UPDATE cupones
                            set cantUsos = cantUsos + 1
                            where idCupon = ?;
                            """;
        
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            ps.setString(1, cupon.getIdCupon());
            
            if(ps.executeUpdate() == 0){
                LOG.warning("No se pudo incrementar el cupon: "+cupon.getIdCupon());
                throw new PersistenciaException("No fue posible utilizar el cupon: "+cupon.getIdCupon());
            }
            cupon.setCantidadUsos(cupon.getCantidadUsos()+1);
            return cupon;
            
            
        } catch (SQLException ex) {
            LOG.severe("Error SQL al incrementar los usos del cupón: "+ex);
            throw new PersistenciaException("Hubo un error al utilizar el cupón", ex);
        }
    }
    
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
            LOG.severe("Error al incrementar los usos");
            throw new PersistenciaException("Error al extraer el cupón.", ex);
        }
    }
    
    
    
    
}
