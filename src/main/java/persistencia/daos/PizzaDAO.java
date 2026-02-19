/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.PizzaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;

/**
 *
 * @author RAYMUNDO
 */
public class PizzaDAO implements IPizzaDAO {
    
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
    private static final Logger LOG = Logger.getLogger(PedidoDAO.class.getName());
    
    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public PizzaDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    public List<PizzaDTO> obtenerProductos() {

        List<PizzaDTO> lista = new ArrayList<>();

        String sql = "SELECT idPizza, nombre, precio, tamaño, descripcion FROM pizzas";

        try (Connection con = conexionBD.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                PizzaDTO producto = new PizzaDTO();
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setTamanio(rs.getString("tamaño"));
                producto.setDescripcion(rs.getString("descripcion"));

                lista.add(producto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}