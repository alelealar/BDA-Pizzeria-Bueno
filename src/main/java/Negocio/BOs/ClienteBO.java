/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.IClienteDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */

public class ClienteBO implements IClienteBO{
    private IClienteDAO clienteDAO;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    public ClienteBO(IClienteDAO tecnico) {
        this.clienteDAO = tecnico;
    }
    
    public Cliente registrarCliente(Cliente cliente) throws NegocioException {
        
        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            LOG.warning("El campo nombre se dejó vacio");
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        
        if (cliente.getApellidoPaterno() == null || cliente.getApellidoPaterno().isBlank()) {
            LOG.warning("El campo apellido paterno se dejó vacio");
            throw new IllegalArgumentException("El apellido paterno del cliente es obligatorio");
        }
        
        if(cliente.getFechaNacimiento().isAfter(LocalDate.now())){
            LOG.warning("Fecha de nacimiento erronea");
            throw new IllegalArgumentException("Favor de ingresar una fecha de nacimiento válida");
        }
        
        if(cliente.getFechaNacimiento() == null){
            LOG.warning("Campoi fecha de nacimiento vacío");
            throw new IllegalArgumentException("El campo fecha de nacimiento es obligatorio.");
        }
        
        if(cliente.getDomicilio() == null){
            LOG.warning("El campo apellido paterno se dejó vacio");
            throw new IllegalArgumentException("El apellido paterno del cliente es obligatorio");
        }
        
        try {
            return clienteDAO.agregarCliente(cliente);
        } catch (PersistenciaException ex) {
            LOG.severe("Error al insertar añ cliente en la BD");
            throw new NegocioException("No se pudo registrar al cliente. Intente más tarde.", ex);
        }
    }

    public Cliente actualizarCliente(Cliente cliente) throws NegocioException {
        // Validaciones de negocio
        if (cliente.getIdCliente() <= 0) {
            throw new IllegalArgumentException("Cliente inválido");
        }

        try {
            return clienteDAO.actualizarCliente(cliente);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Cliente obtenerClientePorId(int id) throws NegocioException {
        if(id < 0){
            LOG.warning("id invalido. Menor a 0");
            throw new NegocioException("El id es invalido. Ingrese un id mayor a 0");
        }
        try {
            return clienteDAO.buscarClientePorId(id);
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void agregarTelefono(int idCliente, Telefono telefono) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminarTelefono(int idCliente, Telefono telefono) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
