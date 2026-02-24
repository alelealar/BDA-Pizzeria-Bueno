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

public class CarritoBO implements ICarritoBO {

    private final ICarritoDAO carritoDAO;
    private final IDetalleCarritoDAO detalleCarritoDAO;
    private final IPizzaDAO pizzaDAO;

    public CarritoBO(ICarritoDAO carritoDAO, IDetalleCarritoDAO detalleCarritoDAO, IPizzaDAO pizzaDAO) {

        this.carritoDAO = carritoDAO;
        this.detalleCarritoDAO = detalleCarritoDAO;
        this.pizzaDAO = pizzaDAO;
    }

    public CarritoDTO obtenerOCrearCarritoEntidad(int idUsuario) throws PersistenciaException {
        //1. buscar carrito
        Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
        if (carrito == null) {
            //si el carrito no existe hay que crearlo
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

    @Override
    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException {

        try {
            // 1. Buscar carrito activo
            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);

            // 2. Si no existe, crearlo
            if (carrito == null) {
                carrito = new Carrito();
                carrito.setIdUsuario(idUsuario);
                carrito.setActivo(true);
                carrito = carritoDAO.crearCarrito(carrito);
            }

            DetalleCarrito detalle = new DetalleCarrito();
            detalle.setIdCarrito(carrito.getIdCarrito());
            detalle.setIdPizza(idPizza);
            detalle.setCantidad(cantidad);
            detalle.setTamanio(tamanio);
            detalle.setNota(nota);

            //Enviarto al dao
            detalleCarritoDAO.agregarProducto(detalle);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar producto al carrito", e);
        }
    }

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
     *
     * @param idUsuario
     * @throws NegocioException
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
}
