package negocio.bos;

import Negocio.DTOs.DetalleCarritoDTO;
import Negocio.excepciones.NegocioException;
import Negocio.BOs.ICarritoBO;
import Negocio.DTOs.CarritoDTO;
import Negocio.DTOs.ClienteDTO;
import persistencia.daos.ICarritoDAO;
import persistencia.daos.IDetalleCarritoDAO;
import persistencia.daos.IPizzaDAO;
import persistencia.dominio.Carrito;
import persistencia.dominio.DetalleCarrito;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

import java.util.ArrayList;
import java.util.List;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.daos.CarritoDAO;
import persistencia.daos.DetalleCarritoDAO;
import persistencia.daos.PizzaDAO;

/**
 * Clase de lógica de negocio encargada de gestionar las operaciones
 * relacionadas con el carrito de compras dentro del sistema.
 *
 * <p>
 * Esta clase implementa la interfaz ICarritoBO y actúa como intermediaria entre
 * la capa de presentación y la capa de persistencia (DAO). Se encarga de
 * aplicar las reglas de negocio antes de interactuar con la base de datos.
 * </p>
 *
 * <h2>Responsabilidades principales:</h2>
 * <ul>
 * <li>Crear o recuperar un carrito activo para un usuario.</li>
 * <li>Crear o recuperar un carrito Express mediante token.</li>
 * <li>Agregar productos al carrito.</li>
 * <li>Actualizar cantidades si el producto ya existe.</li>
 * <li>Obtener el carrito completo con sus detalles.</li>
 * <li>Finalizar (desactivar) un carrito.</li>
 * </ul>
 *
 * @author Brian Kaleb Sandoval Rodríguez
 * @author Paulina Michel Guevara Cervantes
 * @author Alejandra Leal Armenta
 */
public class CarritoBO implements ICarritoBO {

    private final ICarritoDAO carritoDAO;
    private final IDetalleCarritoDAO detalleCarritoDAO;
    private final IPizzaDAO pizzaDAO;

    /**
     * Constructor que recibe las dependencias necesarias para operar.
     *
     * @param carritoDAO DAO encargado de la persistencia de Carrito.
     * @param detalleCarritoDAO DAO encargado de la persistencia de
     * DetalleCarrito.
     * @param pizzaDAO DAO encargado de la persistencia de Pizza.
     */
    public CarritoBO(ICarritoDAO carritoDAO, IDetalleCarritoDAO detalleCarritoDAO, IPizzaDAO pizzaDAO) {

        this.carritoDAO = carritoDAO;
        this.detalleCarritoDAO = detalleCarritoDAO;
        this.pizzaDAO = pizzaDAO;
    }

    /**
     * Obtiene el carrito activo de un usuario. Si no existe, lo crea
     * automáticamente.
     *
     * @param idUsuario Identificador del usuario.
     * @return CarritoDTO con la información básica del carrito.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    public CarritoDTO obtenerOCrearCarritoEntidad(int idUsuario) throws PersistenciaException {
        Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setIdUsuario(idUsuario);
            carrito.setActivo(true);
            carrito = carritoDAO.crearCarrito(carrito);
        }
        CarritoDTO carritoDto = new CarritoDTO();
        carritoDto.setIdCarrito(carrito.getIdCarrito());
        carritoDto.setIdUsuario(carrito.getIdUsuario());

        return carritoDto;
    }

    /**
     * Agrega un producto al carrito de un usuario registrado. Si el carrito no
     * existe, lo crea. Si el producto ya existe con el mismo tamaño, actualiza
     * la cantidad.
     *
     * @param idUsuario ID del usuario.
     * @param idPizza ID de la pizza.
     * @param tamanio Tamaño seleccionado.
     * @param cantidad Cantidad a agregar.
     * @param nota Nota adicional del producto.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException {

        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);

            if (carrito == null) {
                carrito = new Carrito();
                carrito.setIdUsuario(idUsuario);
                carrito.setActivo(true);
                carrito = carritoDAO.crearCarrito(carrito);
            }

            DetalleCarrito detalleExistente = detalleCarritoDAO.obtenerDetalle(carrito.getIdCarrito(), idPizza, tamanio);

            if (detalleExistente != null) {

                int nuevaCantidad = detalleExistente.getCantidad() + cantidad;

                detalleCarritoDAO.actualizarCantidad(
                        detalleExistente.getIdDetalleCarrito(),
                        nuevaCantidad
                );

            } else {

                DetalleCarrito detalle = new DetalleCarrito();
                detalle.setIdCarrito(carrito.getIdCarrito());
                detalle.setIdPizza(idPizza);
                detalle.setCantidad(cantidad);
                detalle.setTamanio(tamanio);
                detalle.setNota(nota);

                detalleCarritoDAO.agregarProducto(detalle);
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al agregar producto al carrito", ex);
        }
    }

    /**
     * Agrega un producto a un carrito Express identificado por token. Si el
     * carrito no existe, se crea automáticamente.
     *
     * @param token Token identificador del carrito Express.
     * @param idPizza ID de la pizza.
     * @param tamanio Tamaño seleccionado.
     * @param cantidad Cantidad a agregar.
     * @param nota Nota adicional.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public void agregarProductoExpress(String token, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException {

        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoExpress(token);

            if (carrito == null) {
                carrito = new Carrito();
                carrito.setToken(token);
                carrito.setActivo(true);
                carrito = carritoDAO.crearCarritoExpress(carrito);
            }

            DetalleCarrito detalleExistente = detalleCarritoDAO.obtenerDetalle(carrito.getIdCarrito(), idPizza, tamanio);

            if (detalleExistente != null) {

                int nuevaCantidad = detalleExistente.getCantidad() + cantidad;

                detalleCarritoDAO.actualizarCantidad(
                        detalleExistente.getIdDetalleCarrito(),
                        nuevaCantidad
                );

            } else {

                DetalleCarrito detalle = new DetalleCarrito();
                detalle.setIdCarrito(carrito.getIdCarrito());
                detalle.setIdPizza(idPizza);
                detalle.setCantidad(cantidad);
                detalle.setTamanio(tamanio);
                detalle.setNota(nota);

                detalleCarritoDAO.agregarProducto(detalle);
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al agregar producto al carrito", ex);
        }
    }

    /**
     * Obtiene un carrito Express activo mediante token. Si no existe, lo crea.
     *
     * @param token Token único del carrito Express.
     * @return CarritoDTO con la información básica.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public CarritoDTO obtenerOCrearCarritoExpress(String token) throws NegocioException {
        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoExpress(token);
            if (carrito == null) {
                carrito = new Carrito();
                carrito.setToken(token);
                carrito.setActivo(true);
                carrito = carritoDAO.crearCarrito(carrito);
            }
            CarritoDTO carritoDto = new CarritoDTO();
            carritoDto.setIdCarrito(carrito.getIdCarrito());
            carritoDto.setToken(carrito.getToken());

            return carritoDto;
        } catch (PersistenciaException ex) {
            System.getLogger(CarritoBO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            throw new NegocioException("Error al obtener carrito Express" + ex.getMessage());
        }
    }

    /**
     * Obtiene el carrito completo de un usuario, incluyendo todos sus detalles.
     *
     * @param idUsuario ID del usuario.
     * @return CarritoDTO con lista de detalles o null si no existe.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public CarritoDTO obtenerCarritoCompleto(int idUsuario) throws NegocioException {

        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
            if (carrito == null) {
                return null;
            }
            List<DetalleCarrito> detalles = detalleCarritoDAO.obtenerDetallesPorCarrito(carrito.getIdCarrito());
            return convertirDTO(carrito, detalles);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener carrito.", ex);
        }
    }

    /**
     * Obtiene el carrito completo Express incluyendo sus detalles.
     *
     * @param token Token del carrito Express.
     * @return CarritoDTO con lista de detalles o null si no existe.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public CarritoDTO obtenerCarritoCompletoExpress(String token) throws NegocioException {

        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoExpress(token);
            if (carrito == null) {
                return null;
            }
            List<DetalleCarrito> detalles = detalleCarritoDAO.obtenerDetallesPorCarrito(carrito.getIdCarrito());
            return convertirDTO(carrito, detalles);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener carrito.", ex);
        }
    }

    /**
     * Finaliza (desactiva) el carrito activo de un usuario.
     *
     * @param idUsuario ID del usuario.
     * @throws NegocioException Si no existe carrito activo o hay error.
     */
    @Override
    public void finalizarCarrito(int idUsuario) throws NegocioException {

        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);

            if (carrito == null) {
                throw new NegocioException("No hay carrito activo.");
            }
            carritoDAO.desactivarCarrito(carrito.getIdCarrito());
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al finalizar carrito.", ex);
        }
    }

    /**
     * Convierte una entidad Carrito y su lista de detalles en un objeto
     * CarritoDTO para la capa de presentación.
     */
    private CarritoDTO convertirDTO(Carrito carrito, List<DetalleCarrito> detalles) {
        CarritoDTO dto = new CarritoDTO();
        dto.setIdCarrito(carrito.getIdCarrito());
        dto.setIdUsuario(carrito.getIdUsuario());
        dto.setFechaCreacion(carrito.getFechaCreacion());
        dto.setActivo(carrito.isActivo());

        List<DetalleCarritoDTO> listaDTO = new ArrayList<>();

        for (DetalleCarrito d : detalles) {
            try {
                DetalleCarritoDTO detDTO = new DetalleCarritoDTO();
                detDTO.setIdDetalleCarrito(d.getIdDetalleCarrito());
                detDTO.setIdPizza(d.getIdPizza());
                detDTO.setCantidad(d.getCantidad());
                detDTO.setTamanio(d.getTamanio());
                detDTO.setNota(d.getNota());

                Pizza pizza = pizzaDAO.obtenerPizzaPorId(d.getIdPizza());
                if (pizza != null) {
                    detDTO.setPrecioUnitario(pizza.getPrecio());
                    detDTO.setNombrePizza(pizza.getNombre());
                }

                listaDTO.add(detDTO);
            } catch (PersistenciaException ex) {
                java.util.logging.Logger.getLogger(CarritoBO.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        dto.setDetalles(listaDTO);
        return dto;
    }

    /**
     * Finaliza (desactiva) un carrito Express.
     *
     * @param token Token del carrito Express.
     * @throws NegocioException Si ocurre un error.
     */
    @Override
    public void finalizarExpress(String token) throws NegocioException {
        try {
            Carrito carrito = carritoDAO.obtenerCarritoActivoExpress(token);

            if (carrito == null) {
                throw new NegocioException("No hay carrito activo.");
            }
            carritoDAO.desactivarCarrito(carrito.getIdCarrito());
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al finalizar carrito.", ex);
        }
    }
}
