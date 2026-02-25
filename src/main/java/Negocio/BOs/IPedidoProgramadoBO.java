/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PedidoProgramadoDTO;
import Negocio.excepciones.NegocioException;

/**
 *
 * @author RAYMUNDO
 */
public interface IPedidoProgramadoBO {
    
    public PedidoProgramadoDTO agregarPedidoProgramado(PedidoProgramadoDTO dto) throws NegocioException;
    
}
