/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package Negocio.BOs;

import Negocio.DTOs.TelefonoDTO;
import Negocio.excepciones.NegocioException;

/**
 *
 * @author Alejandra Leal Armenta, 262719
 */
public interface ITelefonoBO {
    
    
    TelefonoDTO agregarTelefono(String etiqueta, String telefono, int idCliente)throws NegocioException;
    
    void eliminarTelefono(int idCliente, String telefono)throws NegocioException;

}
