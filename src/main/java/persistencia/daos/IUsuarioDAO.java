/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.UsuarioDTO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public interface IUsuarioDAO {
    
    UsuarioDTO iniciarSesion(String usuario, String contraseña) throws PersistenciaException;
    
}
