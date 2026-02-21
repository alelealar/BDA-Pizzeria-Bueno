/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import persistencia.dominio.Carrito;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface ICarritoDAO {
    
    Carrito crearCarrito(Carrito carrito) throws PersistenciaException;

    Carrito obtenerCarritoActivoPorUsuario(int idUsuario) throws PersistenciaException;

    void desactivarCarrito(int idCarrito) throws PersistenciaException;
    
}
