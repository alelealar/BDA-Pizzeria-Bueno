/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IClienteBO {

    public Cliente registrarCliente(Cliente cliente) throws NegocioException;
    
    public void agregarTelefono(int idCliente, Telefono telefono) throws NegocioException;
    
    public void eliminarTelefono(int idCliente, Telefono telefono) throws NegocioException;

    
}
