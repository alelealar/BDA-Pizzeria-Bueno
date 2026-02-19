/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia.daos;

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
    
    List<PedidoProgramado> obtenerPedidosPorEstado(String estado) throws PersistenciaException;
    
    List<PedidoProgramado> obtenerPedidosPorPeriodo(java.util.Date fechaInicio, java.util.Date fechaFin) throws PersistenciaException;

    int contarPedidosActivosPorCliente(int idCliente) throws PersistenciaException;
   
    void actualizarEstado(int idPedido, String nuevoEstado) throws PersistenciaException;

    void marcarComoListo(int idPedido) throws PersistenciaException;

    void marcarComoEntregado(int idPedido) throws PersistenciaException;

    void cancelarPedido(int idPedido) throws PersistenciaException;
    
    
    
    
}
