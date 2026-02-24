/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface ITelefonoDAO {
    Telefono agregarTelefono(Telefono telefono) throws PersistenciaException;
    
    void eliminarTelefono(int idTelefono);
}
