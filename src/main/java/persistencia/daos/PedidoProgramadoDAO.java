
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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class PedidoProgramadoDAO implements IPedidoProgramadoDAO{
    
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
    public PedidoProgramadoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    public PedidoProgramado agregarPedidoProgramado(PedidoProgramado pp) throws PersistenciaException{
        String comandoSQL = """
                            INSERT INTO PedidosProgramados (numPedido, idCupon, idUsuario) 
                            VALUES (?, ?, ?);
                            """;
        
        try(Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            ps.setInt(1, pp.getNumPedido());
            ps.setInt(2, pp.getCupon().getIdCupon());
            ps.setInt(3, pp.getCliente().getIdCliente());
            
            int filasInsertadas = ps.executeUpdate();
            if (filasInsertadas == 0) {
                LOG.warning("No se pudo insertar el pedidoProgramado: " + pp);
                throw new PersistenciaException("No se logró realizar el pedido");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pp.setIdPedido(rs.getInt(1));
                } else {
                    throw new PersistenciaException("Error al obtener el ID generado del nuevo cliente.");
                }
            }

            LOG.info("Pedido Programado Insertado con exito");
            return pp;
            
            
        }   catch (SQLException ex) {
            LOG.severe("Error SQL al insertar al pedido programado: " + ex);
            throw new PersistenciaException("Error al insertar el pedido en la base de datos", ex);
        }
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorCliente(int idCliente) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorTelefono(String telefono) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorEstado(String estado) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosPorPeriodo(Date fechaInicio, Date fechaFin) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int contarPedidosActivosPorCliente(int idCliente) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizarEstado(int idPedido, String nuevoEstado) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void marcarComoListo(int idPedido) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void marcarComoEntregado(int idPedido) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void cancelarPedido(int idPedido) throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<PedidoProgramado> obtenerPedidosProgramados() throws PersistenciaException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
