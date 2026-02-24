package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import Negocio.excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author RAYMUNDO
 */
public interface IPizzaBO {

    public List<PizzaDTO> obtenerProductos() throws NegocioException;
    
    public PizzaDTO obtenerPizzaPorId(int id) throws NegocioException;
}
