/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.BOs;

import Negocio.DTOs.ClienteDTO;
import Negocio.DTOs.UsuarioDTO;
import Negocio.excepciones.NegocioException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.IClienteDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Domicilio;
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
    
    public ClienteDTO registrarCliente(ClienteDTO cliente, String usuario, String contrasena) throws NegocioException {
        if (cliente == null) {
            LOG.warning("Cliente nulo");
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            LOG.warning("El campo nombre se dejó vacío");
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        if (cliente.getCalle() != null && !cliente.getCalle().isBlank()) {
            if (cliente.getNumero() == null || cliente.getNumero().isBlank()
                    || cliente.getColonia() == null || cliente.getColonia().isBlank()
                    || cliente.getCP() == null || cliente.getCP().isBlank()) {
                LOG.warning("Domicilio incompleto");
                throw new NegocioException("Debe proporcionar el domicilio completo si empieza a llenarlo");
            }
        }

        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.of(cliente.getAnio(), cliente.getMesNum(), cliente.getDia());
        } catch (DateTimeException e) {
            LOG.warning("Fecha de nacimiento inválida");
            throw new NegocioException("La fecha de nacimiento no es válida.");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            LOG.warning("Fecha de nacimiento futura");
            throw new NegocioException("La fecha de nacimiento no puede ser posterior a hoy.");
        }
        
        if(usuario.isBlank() || usuario == null){
            LOG.warning("no se ingreso usuario, error");
            throw new NegocioException("Ingrese un usuario");
        }
        
        if(contrasena.isBlank() || contrasena == null){
            LOG.warning("No se ingreso ninguna contraseña");
            throw new NegocioException("Contraseña obligatoria.");
        }

        try {
            Cliente clienteDominio = mapearDTOaDominio(cliente);

            Cliente clienteRegistrado = clienteDAO.agregarCliente(clienteDominio);

            ClienteDTO resultado = mapearDominioaDTO(clienteRegistrado);

            return resultado;

        } catch (PersistenciaException ex) {
            LOG.severe("Error al insertar al cliente en la BD");
            throw new NegocioException("No se pudo registrar al cliente. Intente más tarde.", ex);
        }
    }

    public ClienteDTO actualizarCliente(ClienteDTO cliente) throws NegocioException {
        if (cliente == null) {
            LOG.warning("Cliente nulo");
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }

        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            LOG.warning("El campo nombre se dejó vacío");
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        if (cliente.getCalle() != null && !cliente.getCalle().isBlank()) {
            if (cliente.getNumero() == null || cliente.getNumero().isBlank()
                    || cliente.getColonia() == null || cliente.getColonia().isBlank()
                    || cliente.getCP() == null || cliente.getCP().isBlank()) {
                LOG.warning("Domicilio incompleto");
                throw new NegocioException("Debe proporcionar el domicilio completo si empieza a llenarlo");
            }
        }

        try {
            Cliente clienteDominio = mapearDTOaDominio(cliente);

            Cliente clienteActualizado = clienteDAO.actualizarCliente(clienteDominio);

            ClienteDTO resultado = mapearDominioaDTO(clienteActualizado);

            return resultado;

        } catch (PersistenciaException ex) {
            LOG.severe("Error al actualizar el cliente en la BD");
            throw new NegocioException("No se pudo actualizar al cliente. Intente más tarde.", ex);
        }
    }

    public Cliente obtenerClientePorId(int id) throws NegocioException {
        /*
        if(id < 0){
            LOG.warning("id invalido. Menor a 0");
            throw new NegocioException("El id es invalido. Ingrese un id mayor a 0");
        }
        try {
            Cliente cliente = 
            mapearDTOaDominio(dto);
            return 
        } catch (PersistenciaException ex) {
            Logger.getLogger(ClienteBO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;*/
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
    
    private Cliente mapearDTOaDominio(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(dto.getIdCliente());
        cliente.setNombres(dto.getNombres());
        cliente.setApellidoPaterno(dto.getApellidoPaterno());
        
        LocalDate fechaNacimiento = LocalDate.of(dto.getAnio(), dto.getMesNum(), dto.getDia());
        cliente.setFechaNacimiento(fechaNacimiento);

        Domicilio domicilio = new Domicilio();
        domicilio.setCalle(dto.getCalle());
        domicilio.setNumero(dto.getNumero());
        domicilio.setColonia(dto.getColonia());
        domicilio.setCodigoPostal(dto.getCP());

        cliente.setDomicilio(domicilio);

        return cliente;
    }

    // Convierte de Dominio a DTO
    private ClienteDTO mapearDominioaDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombres(cliente.getNombres());
        dto.setApellidoPaterno(cliente.getApellidoPaterno());
        cliente.setFechaNacimiento(cliente.getFechaNacimiento());

        Domicilio domicilio = cliente.getDomicilio();
        if (domicilio != null) {
            dto.setCalle(domicilio.getCalle());
            dto.setNumero(domicilio.getNumero());
            dto.setColonia(domicilio.getColonia());
            dto.setCP(domicilio.getCodigoPostal());
        }

        return dto;
    }
}
