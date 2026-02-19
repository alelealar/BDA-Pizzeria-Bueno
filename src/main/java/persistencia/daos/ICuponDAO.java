/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.daos;

import java.util.List;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface ICuponDAO {
    
    
    Cupon agregarCupon(Cupon cupon) throws PersistenciaException;
     
    Cupon obtenerCuponPorId(int id) throws PersistenciaException;
    
    Cupon actualizarCupon(Cupon cupon) throws PersistenciaException;
    
    List<Cupon> obtenerCupones() throws PersistenciaException;
    
    List<Cupon> obtenerCuponesPorDescuento() throws PersistenciaException;
}
