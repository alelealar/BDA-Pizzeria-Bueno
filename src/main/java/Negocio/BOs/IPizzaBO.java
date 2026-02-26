package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import Negocio.DTOs.PizzaProductoDTO;
import Negocio.excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface IPizzaBO {

    public List<PizzaDTO> obtenerProductos() throws NegocioException;
    
    public PizzaDTO obtenerPizzaPorId(int id) throws NegocioException;
    
    public ArrayList<PizzaDTO> agruparPizzasPorNombre() throws NegocioException;
    
    PizzaProductoDTO obtenerPizzaPorTamano(String nombre, String tamPizza) throws NegocioException;
    
    void actualizarPrecioPizza(String nombre, String tam, double nuevoPrecio) throws NegocioException;
    
    void actualizarEstadoPizza(int idEstado) throws NegocioException;
}
