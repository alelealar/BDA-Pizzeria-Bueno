package Negocio.BOs;

import Negocio.DTOs.PedidoDTO;
import Negocio.DTOs.PedidoExpressDTO;
import Negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de negocio para la gestion de
 * PedidoExpress.
 *
 * @author Brian
 */
public interface IPedidoExpressBO {

    /**
     * Metodo que permite agregar un nuevo PedidoExpress.
     *
     * Se encarga de validar la informacion antes de enviarla a la capa de
     * persistencia.
     *
     * @param pedto Objeto PedidoExpressDTO con la informacion del pedido.
     * @return PedidoExpressDTO con los datos del pedido agregado.
     * @throws NegocioException Si ocurre un error en las validaciones o en el
     * proceso.
     */
    public PedidoExpressDTO agregarPedidoExpress(PedidoExpressDTO pedto) throws NegocioException;

    /**
     * Metodo que permite obtener un PedidoExpress por su id.
     *
     * @param idPedidoExpress Identificador del pedido express.
     * @return PedidoExpressDTO con la informacion encontrada.
     * @throws NegocioException Si el id no es valido o no se encuentra el
     * pedido.
     */
    public PedidoExpressDTO obtenerPedidoExpressPorId(int idPedidoExpress) throws NegocioException;

    public PedidoExpressDTO actualizarPedidoExpress(PedidoExpressDTO pedto) throws NegocioException;
}
