/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import java.util.List;
import persistencia.daos.IPizzaDAO;
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
        return pizzaDAO.obtenerProductos();
    }
}