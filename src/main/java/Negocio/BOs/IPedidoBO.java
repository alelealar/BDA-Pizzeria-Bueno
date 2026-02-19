package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import persistencia.dominio.Pedido;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPedidoBO {

    Pedido agregarPedido(Pedido pedido) throws NegocioException;

    Pedido actualizarPedido(Pedido pedido) throws NegocioException;

    Pedido buscarPedidoPorId(int id) throws NegocioException;

}
