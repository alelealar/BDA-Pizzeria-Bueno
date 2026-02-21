/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.PizzaDTO;
import java.util.List;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface IPizzaDAO {

    public List<Pizza> obtenerPizzas() throws PersistenciaException;
    public Pizza obtenerPizzaPorId(int id) throws PersistenciaException;
    
}
