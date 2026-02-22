/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import Negocio.excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.IPizzaDAO;
import persistencia.daos.PizzaDAO;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */

public class PizzaBO implements IPizzaBO {

    private final IPizzaDAO pizzaDAO;

    public PizzaBO(IPizzaDAO pizzaDAO) {
        this.pizzaDAO = pizzaDAO;
    }
    
    public PizzaBO() {
        this.pizzaDAO = new PizzaDAO();
    }

    @Override
    public List<PizzaDTO> obtenerProductos() throws NegocioException {
        List<Pizza> pizzas = null; 
        try {
            pizzas = pizzaDAO.obtenerPizzas();
        } catch (PersistenciaException ex) {
            Logger.getLogger(PizzaBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<PizzaDTO> dtos = new ArrayList<>();
        
        for (Pizza p : pizzas) {
            PizzaDTO dto = new PizzaDTO();
            dto.setIdPizza(p.getIdPizza());
            dto.setNombre(p.getNombre());
            dto.setTamanio(p.getTamanio());
            dto.setDescripcion(p.getDescripcion());
            dto.setPrecio(p.getPrecio());
            
            if (p.getEstado() == Pizza.EstadoPizza.DISPONIBLE) {
                dto.setEstado(PizzaDTO.EstadoPizza.DISPONIBLE);
            } else {
                dto.setEstado(PizzaDTO.EstadoPizza.NO_DISPONIBLE);
            }
            
            dtos.add(dto);
        }
    
        // 4. Devuelves la lista de DTOs. ¡Ahora sí la UI está protegida!
        return dtos;
    }
}