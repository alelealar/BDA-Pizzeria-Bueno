/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.UsuarioDTO;
import Negocio.excepciones.NegocioException;
import persistencia.daos.IUsuarioDAO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class UsuarioBO implements IUsuarioBO {
    
    private final IUsuarioDAO usuarioDAO;

    public UsuarioBO(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UsuarioDTO iniciarSesion(String usuario, String contrasenia) throws NegocioException {
        if (usuario == null || usuario.isBlank()) {
            throw new NegocioException("El usuario es obligatorio");
        }

        if (contrasenia == null || contrasenia.isBlank()) {
            throw new NegocioException("La contraseña es obligatoria");
        }

        try {
            UsuarioDTO usuarioDTO = usuarioDAO.iniciarSesion(usuario, contrasenia);

            if (usuarioDTO == null) {
                throw new NegocioException("Usuario o contraseña incorrectos");
            }

            return usuarioDTO;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al validar credenciales", e);
        }
    }
}