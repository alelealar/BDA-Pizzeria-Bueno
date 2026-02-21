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
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

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
    private static final Logger LOG = Logger.getLogger(PizzaDAO.class.getName());
    
    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public PizzaDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    public List<Pizza> obtenerPizzas() throws PersistenciaException {

        List<Pizza> lista = new ArrayList<>();

        String comandoSQL = """
                            SELECT
                                idPizza, 
                                nombre, 
                                tamaño, 
                                descripcion, 
                                precio, 
                                rutaImagen, 
                                estado
                            FROM pizzas
                            """;

        try (Connection con = conexionBD.crearConexion(); PreparedStatement ps = con.prepareStatement(comandoSQL)){
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pizza pizza = extraerPizza(rs);

                    lista.add(pizza);
                
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PizzaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return lista;
    }
    
    public Pizza obtenerPizzaPorId(int id) throws PersistenciaException{
        String comandoSQL = """
                            SELECT
                            	idPizza, 
                                nombre, 
                                tamaño, 
                                descripcion, 
                                precio, 
                                rutaImagen, 
                                estado
                            FROM pizzas
                            where idPizza = ?;
                            """;
        
        try (Connection conn = this.conexionBD.crearConexion(); PreparedStatement ps = conn.prepareStatement(comandoSQL)){
            ps.setInt(1, id);
            
            try(ResultSet rs = ps.executeQuery()){
                if(!rs.next()){
                    LOG.warning("NO existe una pizza con el id {0}");
                    throw new PersistenciaException("La pizza a buscar no existe en el catálogo");
                }
            return extraerPizza(rs);    
                
            }
        } catch (SQLException ex) {
            LOG.severe("Error al obtener la pizza {0} de la base de datos");
            throw new PersistenciaException("Hubo un error al buscar la Pizza");
        }
    }
    
    
    private Pizza extraerPizza(ResultSet rs) throws PersistenciaException {
        try {
           Pizza pizza = new Pizza();
           pizza.setNombre(rs.getString("nombre"));
           pizza.setTamanio(rs.getString("Tamaño"));
           pizza.setDescripcion(rs.getString("descripcion"));
           pizza.setPrecio(rs.getDouble("precio"));
           pizza.setRutaImagen(rs.getString("rutaImagen"));
                      
           pizza.setEstado(Pizza.EstadoPizza.valueOf(rs.getString("estado").toUpperCase()));
           
           return pizza;
                    
        } catch (SQLException ex) {
            LOG.severe("Error al extraer la pizza. " + ex.getMessage());
            throw new PersistenciaException("Error al extraer la pizza.");
        } catch (IllegalArgumentException ex) {
            LOG.severe("Estado de pizza inválido en BD: " + ex.getMessage());
            throw new PersistenciaException("Estado de pizza inválido.");
        }
    }
    
}