/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.CuponDTO;
import Negocio.excepciones.NegocioException;

/**
 *
 * @author RAYMUNDO
 */
public interface ICuponBO {
    
    public CuponDTO validarCupon(String codigo) throws NegocioException;
    
}
