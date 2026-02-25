/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package persistencia.daos;

import java.util.logging.Logger;
import persistencia.conexion.IConexionBD;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Alejandra Leal Armenta, 262719
 */
public interface ITelefonoDAO {
    
    
    Telefono agregarTelefono(Telefono telefono) throws PersistenciaException;
    
    Telefono eliminarTelefono(int idTelefono) throws PersistenciaException;

    Telefono obtenerTelefono(int idTelefono) throws PersistenciaException;
}
