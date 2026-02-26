package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.IDetalleCarritoDAO;
import persistencia.dominio.DetalleCarrito;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author sando
 */
public class DetalleCarritoBO implements IDetalleCarritoBO {

    private static final Logger LOG = Logger.getLogger(DetalleCarritoBO.class.getName());
    private final IDetalleCarritoDAO detalleCarritoDAO;

    /**
     * Constructor que inyecta la dependencia del DAO.
     *
     * * @param detalleCarritoDAO Instancia del DAO de detalles de carrito.
     */
    public DetalleCarritoBO(IDetalleCarritoDAO detalleCarritoDAO) {
        this.detalleCarritoDAO = detalleCarritoDAO;
    }

    @Override
    public void agregarProducto(DetalleCarrito detalle) throws NegocioException {
        try {
            // Ejemplo de regla de negocio: la cantidad debe ser mayor a 0
            if (detalle.getCantidad() <= 0) {
                throw new NegocioException("La cantidad del producto debe ser mayor a cero.");
            }

            detalleCarritoDAO.agregarProducto(detalle);
            LOG.info("Producto agregado al carrito con éxito.");

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en la capa de persistencia al agregar producto", ex);
            throw new NegocioException("No se pudo agregar el producto al carrito.", ex);
        }
    }

    @Override
    public DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio) throws NegocioException {
        try {
            return detalleCarritoDAO.obtenerDetalle(idCarrito, idPizza, tamanio);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en la capa de persistencia al obtener el detalle", ex);
            throw new NegocioException("No se pudo obtener la información del producto en el carrito.", ex);
        }
    }

    @Override
    public void eliminarDetallesPorCarrito(int idDetalleCarrito) throws NegocioException {
        try {
            if (idDetalleCarrito <= 0) {
                throw new NegocioException("El ID del carrito no es válido.");
            }
            detalleCarritoDAO.eliminarDetalleCarrito(idDetalleCarrito);

        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en la capa de persistencia al eliminar detalle", ex);
            throw new NegocioException("No se pudo eliminar el detalle del carrito.", ex);
        }
    }

    @Override
    public void actualizarCantidad(int idDetalle, int nuevaCantidad) throws NegocioException {
        try {
            System.out.println(String.valueOf(idDetalle)+ String.valueOf(nuevaCantidad));
            // Regla de negocio
            if (nuevaCantidad < 1) {
                throw new NegocioException("La cantidad mínima permitida es 1.");
            }

            detalleCarritoDAO.actualizarCantidad(idDetalle, nuevaCantidad);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en la capa de persistencia al actualizar cantidad", ex);
            throw new NegocioException("No se pudo actualizar la cantidad del producto.", ex);
        }
    }

    @Override
    public List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito) throws NegocioException {
        try {
            return detalleCarritoDAO.obtenerDetallesPorCarrito(idCarrito);
        } catch (PersistenciaException ex) {
            LOG.log(Level.SEVERE, "Error en la capa de persistencia al obtener los detalles", ex);
            throw new NegocioException("Error al recuperar los productos del carrito.", ex);
        }
    }
}
