package negocio.bos;

import Negocio.DTOs.DetalleCarritoDTO;
import Negocio.excepciones.NegocioException;
import Negocio.BOs.ICarritoBO;

import persistencia.daos.ICarritoDAO;
import persistencia.daos.IDetalleCarritoDAO;
import persistencia.daos.IPizzaDAO;
import persistencia.dominio.Carrito;
import persistencia.dominio.DetalleCarrito;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

import java.util.ArrayList;
import java.util.List;

public class CarritoBO implements ICarritoBO {
    
    private final ICarritoDAO carritoDAO;
    private final IDetalleCarritoDAO detalleDAO;
    private final IPizzaDAO pizzaDAO;

    public CarritoBO(ICarritoDAO carritoDAO, IDetalleCarritoDAO detalleDAO, IPizzaDAO pizzaDAO) {
        this.carritoDAO = carritoDAO;
        this.detalleDAO = detalleDAO;
        this.pizzaDAO = pizzaDAO;
    }

    @Override
    public void agregarProducto(int idUsuario, int idPizza, String tamanio, int cantidad, String nota) throws NegocioException {

        if (cantidad <= 0) throw new NegocioException("La cantidad debe ser mayor a 0");

        try {

            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);

            if (carrito == null) {
                carrito = new Carrito();
                carrito.setIdUsuario(idUsuario);
                carrito = carritoDAO.crearCarrito(carrito);
            }

            DetalleCarrito detalleExistente = detalleDAO.obtenerDetalle(carrito.getIdCarrito(), idPizza, tamanio);

            if (detalleExistente != null) {
                int nuevaCantidad = detalleExistente.getCantidad() + cantidad;
                detalleDAO.actualizarCantidad(detalleExistente.getIdDetalleCarrito(), nuevaCantidad);
            } else {
                DetalleCarrito nuevo = new DetalleCarrito();
                nuevo.setIdCarrito(carrito.getIdCarrito());
                nuevo.setIdPizza(idPizza);
                nuevo.setTamanio(tamanio);
                nuevo.setCantidad(cantidad);
                nuevo.setNota(nota);
                detalleDAO.agregarProducto(nuevo);
            }

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar producto al carrito", e);
        }
    }

    @Override
    public List<DetalleCarritoDTO> obtenerCarrito(int idUsuario) throws NegocioException {

        try {

            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
            if (carrito == null) return List.of();

            List<DetalleCarrito> detalles = detalleDAO.obtenerDetallesPorCarrito(carrito.getIdCarrito());
            List<DetalleCarritoDTO> lista = new ArrayList<>();

            for (DetalleCarrito d : detalles) {
                Pizza pizza = pizzaDAO.obtenerPizzaPorId(d.getIdPizza());
                double precio = pizza.getPrecio();
                DetalleCarritoDTO dto = new DetalleCarritoDTO(pizza.getIdPizza(), pizza.getNombre(), d.getTamanio(), d.getCantidad(), precio, d.getNota());
                lista.add(dto);
            }

            return lista;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener carrito", e);
        }
    }

    @Override
    public double calcularTotal(int idUsuario) throws NegocioException {
        return obtenerCarrito(idUsuario).stream().mapToDouble(DetalleCarritoDTO::getSubtotal).sum();
    }

    @Override
    public void eliminarProducto(int idUsuario, int idPizza, String tamanio) throws NegocioException {

        try {

            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
            if (carrito == null) return;

            DetalleCarrito detalle = detalleDAO.obtenerDetalle(carrito.getIdCarrito(), idPizza, tamanio);
            if (detalle != null) detalleDAO.eliminarDetallesPorCarrito(detalle.getIdDetalleCarrito());

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al eliminar producto del carrito", e);
        }
    }

    @Override
    public void disminuirCantidad(int idUsuario, int idPizza, String tamanio) throws NegocioException {

        try {

            Carrito carrito = carritoDAO.obtenerCarritoActivoPorUsuario(idUsuario);
            if (carrito == null) return;

            DetalleCarrito detalle = detalleDAO.obtenerDetalle(carrito.getIdCarrito(), idPizza, tamanio);
            if (detalle == null) return;

            int nuevaCantidad = detalle.getCantidad() - 1;

            if (nuevaCantidad <= 0) {
                detalleDAO.eliminarDetallesPorCarrito(detalle.getIdDetalleCarrito());
            } else {
                detalleDAO.actualizarCantidad(detalle.getIdDetalleCarrito(), nuevaCantidad);
            }

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al disminuir cantidad", e);
        }
    }
}