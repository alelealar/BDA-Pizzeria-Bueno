/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
    
}