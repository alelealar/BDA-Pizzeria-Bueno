/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import java.util.List;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface IPizzaBO {
    
    public List<PizzaDTO> obtenerProductos() throws PersistenciaException;
    
}