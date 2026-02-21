/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import java.util.ArrayList;
import java.util.List;
import persistencia.daos.IPizzaDAO;
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

    @Override
    public List<PizzaDTO> obtenerProductos() throws PersistenciaException {
        List<Pizza> pizzas = pizzaDAO.obtenerPizzas(); 
        
        List<PizzaDTO> dtos = new ArrayList<>();
        
        for (Pizza p : pizzas) {
            PizzaDTO dto = new PizzaDTO();
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