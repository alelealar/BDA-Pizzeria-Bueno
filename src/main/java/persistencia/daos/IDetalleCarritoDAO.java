package persistencia.daos;

import java.util.List;
import persistencia.dominio.DetalleCarrito;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link DetalleCarrito}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object) y proporciona los métodos
 * necesarios para gestionar los productos contenidos dentro de un carrito de compras.
 * 
 * Permite agregar productos al carrito, consultar los productos existentes, actualizar
 * cantidades, eliminar productos y obtener detalles específicos de un producto dentro
 * de un carrito.
 * 
 * Las clases que implementen esta interfaz serán responsables de interactuar directamente
 * con la base de datos para realizar estas operaciones.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IDetalleCarritoDAO {

    /**
     * Agrega un producto al carrito en la base de datos.
     * 
     * Este método registra un nuevo detalle de carrito, incluyendo la pizza,
     * el tamaño, la cantidad y cualquier nota adicional.
     * 
     * @param detalle objeto {@link DetalleCarrito} con la información del producto a agregar
     * @throws PersistenciaException si ocurre un error durante la inserción
     */
    public void agregarProducto(DetalleCarrito detalle) throws PersistenciaException;

    /**
     * Obtiene todos los detalles (productos) asociados a un carrito específico.
     * 
     * Este método permite consultar todos los productos que pertenecen a un carrito,
     * incluyendo sus cantidades, tamaños y notas.
     * 
     * @param idCarrito identificador del carrito
     * @return lista de objetos {@link DetalleCarrito} asociados al carrito
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<DetalleCarrito> obtenerDetallesPorCarrito(int idCarrito) throws PersistenciaException;

    /**
     * Elimina un producto específico del carrito.
     * 
     * Este método elimina el registro correspondiente al detalle del carrito
     * identificado por su id.
     * 
     * @param idDetalleCarrito identificador del detalle a eliminar
     * @throws PersistenciaException si ocurre un error durante la eliminación
     */
    public void eliminarDetalleCarrito(int idDetalleCarrito) throws PersistenciaException;

    /**
     * Actualiza la cantidad de un producto dentro del carrito.
     * 
     * Este método modifica el valor de la cantidad de un detalle existente
     * en el carrito.
     * 
     * @param idDetalle identificador del detalle del carrito
     * @param nuevaCantidad nueva cantidad del producto
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    void actualizarCantidad(int idDetalle, int nuevaCantidad) throws PersistenciaException;

    /**
     * Obtiene un detalle específico del carrito basado en el carrito, la pizza y el tamaño.
     * 
     * Este método es útil para verificar si un producto específico ya existe en el carrito.
     * 
     * @param idCarrito identificador del carrito
     * @param idPizza identificador de la pizza
     * @param tamanio tamaño de la pizza
     * @return objeto {@link DetalleCarrito} si existe, o null si no se encuentra
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    DetalleCarrito obtenerDetalle(int idCarrito, int idPizza, String tamanio) throws PersistenciaException;
}