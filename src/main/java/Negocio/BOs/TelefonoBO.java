/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.BOs;

import Negocio.DTOs.TelefonoDTO;
import Negocio.excepciones.NegocioException;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.ITelefonoDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;
/**
 *
 * @author Alejandra Leal Armenta, 262719
 */


public class TelefonoBO implements ITelefonoBO{
    private ITelefonoDAO telefonoDAO;
    private static final Logger LOG = Logger.getLogger(TelefonoBO.class.getName());

    public TelefonoBO(ITelefonoDAO telefono) {
        this.telefonoDAO = telefono;
    }

    @Override
    public TelefonoDTO agregarTelefono(String etiqueta, String telefono, int idCliente) throws NegocioException{
        if(telefono == null || telefono.isBlank()){
            LOG.warning("Se ingreso un telefono nulo o vacio");
            throw new NegocioException("El telefono no debe estar vacío");
        }
        
        if(etiqueta == null || etiqueta.isBlank()){
            LOG.warning("Se ingreso una etiqueta nula o vacia");
            throw new NegocioException("La etiqueta no debe estar vacío");
        }
        
        if(idCliente > 0){
            LOG.warning("id menor a 0. Error");
            throw new NegocioException("el Id debe ser mayor a 0");
        }
        
        try {
            Telefono telefonoDom = new Telefono();
            telefonoDom.setEtiqueta(etiqueta);
            telefonoDom.setTelefono(telefono);
            Cliente cliente = new Cliente();
            cliente.setIdCliente(idCliente);
            telefonoDom.setCliente(cliente);
            
            Telefono telefonoGuardado = telefonoDAO.agregarTelefono(telefonoDom);
            
            TelefonoDTO telDTO = new TelefonoDTO();
            telDTO.setTelefono(telefonoGuardado.getTelefono());
            telDTO.setEtiqueta(telefonoGuardado.getEtiqueta());
            telDTO.setIdUsuario(telefonoGuardado.getCliente().getIdUsuario());
            
            return telDTO;
     
        } catch (PersistenciaException ex) {
            LOG.severe("Error SQL al ingresar el telefono del cliente");
            throw new NegocioException("Hubo un error en el sistema al intentar agregar el telefono");
        }
    }

    @Override
    public void eliminarTelefono(int idCliente, String telefono) throws NegocioException{
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
