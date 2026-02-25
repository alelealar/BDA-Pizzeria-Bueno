/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;

/**
 *
 * @author RAYMUNDO
 */
public class DetallePedidoDAO {
    
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
     public DetallePedidoDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
     
    public void insertarDetalle(DetallePedidoDTO detalle) throws SQLException {
        String sql = """
            INSERT INTO DetallesPedidos (idPedido, idPizza, cantidad, nota)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection con = conexionBD.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdPedido());
            ps.setInt(2, detalle.getIdPizza());
            ps.setInt(3, detalle.getCantidad());
            ps.setString(4, detalle.getNota());

            ps.executeUpdate();
        }
    }
}
