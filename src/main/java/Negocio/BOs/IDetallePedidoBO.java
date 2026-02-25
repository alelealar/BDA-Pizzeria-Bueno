/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.excepciones.NegocioException;

/**
 *
 * @author RAYMUNDO
 */
public interface IDetallePedidoBO {
    
    void agregarDetallePedido(int idPedido, int idPizza, int cantidad, String nota) throws NegocioException;
    
}
