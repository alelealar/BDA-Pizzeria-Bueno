/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.DetalleCarritoDTO;
import Negocio.excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author RAYMUNDO
 */
public interface ICarritoBO {
    
    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException;
    
    public List<DetalleCarritoDTO> obtenerCarrito(int idUsuario) throws NegocioException;
    
    public double calcularTotal(int idUsuario) throws NegocioException;
    
    public void eliminarProducto(int idUsuario, int idPizza, String tamanio) throws NegocioException;
    
    public void disminuirCantidad(int idUsuario, int idPizza, String tamanio) throws NegocioException;
}
