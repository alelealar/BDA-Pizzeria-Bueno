package Negocio.BOs;

import Negocio.DTOs.CarritoDTO;
import Negocio.DTOs.ClienteDTO;
import Negocio.DTOs.DetalleCarritoDTO;
import Negocio.excepciones.NegocioException;
import java.util.List;

/**
 * Interfaz que define las operaciones de negocio relacionadas con la gestión
 * del carrito de compras dentro del sistema.
 *
 * <p>
 * Esta interfaz pertenece a la capa de negocio (BO - Business Object) y
 * establece los métodos necesarios para:
 * </p>
 * <ul>
 * <li>Agregar productos al carrito.</li>
 * <li>Obtener la información completa del carrito.</li>
 * <li>Finalizar compras normales y express.</li>
 * <li>Gestionar carritos asociados a tokens temporales.</li>
 * </ul>
 *
 * <p>
 * Las implementaciones de esta interfaz deberán encargarse de validar la lógica
 * del sistema antes de comunicarse con la capa de persistencia.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez
 * @author Alejandra Leal Armenta
 * @author Paulina Michel Guevara Cervantes
 */
public interface ICarritoBO {

    /**
     * Agrega un producto al carrito de un usuario registrado.
     *
     * @param idUsuario Identificador del usuario.
     * @param idPizza Identificador de la pizza.
     * @param tamanio Tamaño seleccionado.
     * @param cantidad Cantidad solicitada.
     * @param nota Nota adicional.
     * @throws NegocioException Si ocurre un error en la lógica del negocio.
     */
    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException;

    /**
     * Obtiene el carrito completo de un usuario.
     *
     * @param idUsuario Identificador del usuario.
     * @return CarritoDTO con toda la información del carrito.
     * @throws NegocioException Si ocurre un error durante la consulta.
     */
    public CarritoDTO obtenerCarritoCompleto(int idUsuario) throws NegocioException;

    /**
     * Finaliza el carrito de un usuario registrado.
     *
     * @param idUsuario Identificador del usuario.
     * @throws NegocioException Si ocurre un error al finalizar la compra.
     */
    public void finalizarCarrito(int idUsuario) throws NegocioException;

    /**
     * Finaliza un carrito express mediante un token.
     *
     * @param token Token identificador del carrito express.
     * @throws NegocioException Si ocurre un error en el proceso.
     */
    public void finalizarExpress(String token) throws NegocioException;

    /**
     * Agrega un producto a un carrito express.
     *
     * @param token Token del carrito express.
     * @param idPizza Identificador de la pizza.
     * @param tamanio Tamaño seleccionado.
     * @param cantidad Cantidad solicitada.
     * @param nota Nota adicional.
     * @throws NegocioException Si ocurre un error en la lógica.
     */
    public void agregarProductoExpress(String token, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException;

    /**
     * Obtiene o crea un carrito express asociado a un token.
     *
     * @param token Token del carrito.
     * @return CarritoDTO correspondiente.
     * @throws NegocioException Si ocurre un error en la operación.
     */
    public CarritoDTO obtenerOCrearCarritoExpress(String token) throws NegocioException;

    /**
     * Obtiene el carrito completo de una sesión express.
     *
     * @param token Token del carrito express.
     * @return CarritoDTO con la información completa.
     * @throws NegocioException Si ocurre un error durante la consulta.
     */
    public CarritoDTO obtenerCarritoCompletoExpress(String token) throws NegocioException;

}
