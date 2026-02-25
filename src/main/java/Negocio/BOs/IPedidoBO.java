package Negocio.BOs;

import Negocio.DTOs.PedidoDTO;
import Negocio.DTOs.PedidoDetalleDTO;
import Negocio.DTOs.PedidoTablaDTO;
import Negocio.excepciones.NegocioException;
import java.time.LocalDate;
import java.util.List;
import persistencia.dominio.PedidoResumen;

/**
 * Define las operaciones de negocio relacionadas con la entidad Pedido.
 * 
 * Forma parte de la capa de negocio y trabaja con DTOs para
 * desacoplar la presentación de la persistencia.
 */
public interface IPedidoBO {

    /**
     * Agrega un nuevo pedido.
     * 
     * @param pedidoDTO datos del pedido a registrar
     * @return pedido registrado con ID generado
     * @throws NegocioException si ocurre un error de validación o persistencia
     */
    PedidoDTO agregarPedido(PedidoDTO pedidoDTO) throws NegocioException;

    /**
     * Actualiza un pedido existente.
     * 
     * @param pedidoDTO datos actualizados del pedido
     * @return pedido actualizado
     * @throws NegocioException si ocurre un error
     */
    PedidoDTO actualizarPedido(PedidoDTO pedidoDTO) throws NegocioException;

    /**
     * Busca un pedido por su identificador.
     * 
     * @param id identificador del pedido
     * @return pedido encontrado
     * @throws NegocioException si no existe o hay error
     */
    PedidoDTO buscarPedidoPorId(int id) throws NegocioException;

    /**
     * Obtiene todos los pedidos registrados.
     * 
     * @return lista de pedidos
     * @throws NegocioException si ocurre un error
     */
    List<PedidoDTO> obtenerPedidos() throws NegocioException;

    /**
     * Consulta pedidos dentro de un rango de fechas.
     * 
     * @param fechaInicio fecha inicial (inclusive)
     * @param fechaFin fecha final (exclusive)
     * @return lista de pedidos en el rango
     * @throws NegocioException si ocurre un error
     */
    List<PedidoDTO> consultarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) throws NegocioException;
    
    public List<PedidoTablaDTO> obtenerPedidosTabla() throws NegocioException;
    
    public List<PedidoTablaDTO> obtenerPedidosFiltrados(String filtro) throws NegocioException;
    
    public void cambiarEstado(int idPedido, String nuevoEstado) throws NegocioException;
    
    PedidoDetalleDTO obtenerDetallePedido(int idPedido) throws NegocioException;
    
    public boolean validarFolioYPIN(int idPedido, String folio, String pin) throws NegocioException;
    
    public List<PedidoDTO> obtenerPedidosCliente(int idUsuario) throws NegocioException;
}