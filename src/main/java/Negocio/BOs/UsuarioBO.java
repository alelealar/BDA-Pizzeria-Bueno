/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.UsuarioDTO;
import Negocio.excepciones.NegocioException;
import java.util.logging.Logger;
import persistencia.daos.IUsuarioDAO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class UsuarioBO implements IUsuarioBO {
    
    private IUsuarioDAO usuarioDAO;
    private static final Logger LOG = Logger.getLogger(UsuarioBO.class.getName());

    public UsuarioBO(IUsuarioDAO usuario) {
        this.usuarioDAO = usuario; //asignamos valor al DAO
    }   

    @Override
    public UsuarioDTO iniciarSesion(String usuario, String contrasena) throws NegocioException {

        if(usuario == null || usuario.isBlank()) {
            throw new NegocioException("Usuario vacío");
        }
        if(contrasena == null || contrasena.isBlank()){
            throw new NegocioException("Contraseña vacía");
        }

        try {
            UsuarioDTO user = usuarioDAO.iniciarSesion(usuario, contrasena);

            if (user == null) {
                throw new NegocioException("Usuario o contraseña incorrectos");
            }

            return user; // Si todo bien, regresas el usuario logueado
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de sistema: " + e.getMessage());
        }

    }

}