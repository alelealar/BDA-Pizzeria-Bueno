package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import persistencia.dominio.Pedido;

/**
 *
 * @author Brian
 */
public interface IPedidoBO {

    Pedido agregarPedido(Pedido pedido) throws NegocioException;

    Pedido actualizarPedido(Pedido pedido) throws NegocioException;

    Pedido buscarPedidoPorId(int id) throws NegocioException;

}
