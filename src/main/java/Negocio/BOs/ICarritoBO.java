/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.CarritoDTO;
import Negocio.DTOs.ClienteDTO;
import Negocio.DTOs.DetalleCarritoDTO;
import Negocio.excepciones.NegocioException;
import java.util.List;

/**
 *
 * @author RAYMUNDO
 */
public interface ICarritoBO {

    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException;

    public CarritoDTO obtenerCarritoCompleto(int idUsuario) throws NegocioException;

    public void finalizarCarrito(int idUsuario) throws NegocioException;
    
    public void finalizarExpress(String token) throws NegocioException;

    public void agregarProductoExpress(String token, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException;

    public CarritoDTO obtenerOCrearCarritoExpress(String token) throws NegocioException;

    public CarritoDTO obtenerCarritoCompletoExpress(String token) throws NegocioException;

}
