/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import java.util.List;
import persistencia.dominio.DetalleCarrito;

/**
 *
 * @author Brian
 */
public interface IDetalleCarritoBO {

    void agregarProducto(DetalleCarrito detalle) throws NegocioException;

    DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio) throws NegocioException;

    void eliminarDetallesPorCarrito(int idCarrito) throws NegocioException;

    void actualizarCantidad(int idDetalle, int nuevaCantidad) throws NegocioException;

    List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito) throws NegocioException;
}
