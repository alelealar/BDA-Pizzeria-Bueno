/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.DetallePedidoDTO;
import java.sql.SQLException;
import java.util.List;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPedidoProgramadoDAO { 
    
    PedidoProgramado agregarPedidoProgramado(PedidoProgramado pp) throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPedidosProgramados() throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPedidosPorCliente(int idCliente) throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPedidosPorTelefono(String telefono) throws PersistenciaException;
    
    public double calcularTotalProgramado(int idPedido) throws PersistenciaException;
    
    public void insertarDetalle(int idPedido, int idPizza, int cantidad, String nota) throws PersistenciaException;
    
}
