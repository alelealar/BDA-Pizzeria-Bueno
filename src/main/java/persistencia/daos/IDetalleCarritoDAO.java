/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.daos;

import java.util.List;
import persistencia.dominio.DetalleCarrito;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface IDetalleCarritoDAO {
    
    public void agregarProducto(DetalleCarrito detalle) throws PersistenciaException;
    
    public List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito) throws PersistenciaException;
    
    public void eliminarDetallesPorCarrito(int idCarrito) throws PersistenciaException;
    
    void actualizarCantidad(int idDetalle, int nuevaCantidad) throws PersistenciaException;
    
    DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio) throws PersistenciaException;
}
