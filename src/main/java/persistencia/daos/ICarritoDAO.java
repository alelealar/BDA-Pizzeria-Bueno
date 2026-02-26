package persistencia.daos;

import persistencia.dominio.Carrito;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas
 * con la entidad {@link Carrito}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object) y establece
 * los métodos necesarios para crear, consultar y actualizar carritos dentro
 * del sistema.
 * 
 * Permite gestionar tanto carritos normales asociados a un usuario como
 * carritos express asociados a un token temporal.
 * 
 * Las clases que implementen esta interfaz serán responsables de interactuar
 * directamente con la base de datos.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface ICarritoDAO {

    /**
     * Crea un nuevo carrito en la base de datos.
     *
     * Este método registra un carrito asociado a un usuario, estableciéndolo
     * como activo para que pueda agregar productos posteriormente.
     *
     * @param carrito objeto {@link Carrito} que contiene la información del carrito a crear
     * @return el carrito creado con su identificador generado
     * @throws PersistenciaException si ocurre un error durante la operación de persistencia
     */
    Carrito crearCarrito(Carrito carrito) throws PersistenciaException;

    /**
     * Obtiene el carrito activo de un usuario específico.
     *
     * Este método permite recuperar el carrito que actualmente está en uso
     * por un usuario y que aún no ha sido convertido en pedido.
     *
     * @param idUsuario identificador único del usuario
     * @return el carrito activo del usuario o null si no existe
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    Carrito obtenerCarritoActivoPorUsuario(int idUsuario) throws PersistenciaException;

    /**
     * Desactiva un carrito existente en la base de datos.
     *
     * Este método se utiliza cuando un carrito ha sido convertido en pedido
     * o ya no se encuentra en uso, cambiando su estado a inactivo.
     *
     * @param idCarrito identificador único del carrito a desactivar
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    void desactivarCarrito(int idCarrito) throws PersistenciaException;

    /**
     * Crea un nuevo carrito express en la base de datos.
     *
     * Los carritos express son utilizados para pedidos rápidos, generalmente
     * asociados a un token en lugar de un usuario registrado.
     *
     * @param carrito objeto {@link Carrito} que contiene la información del carrito express
     * @return el carrito express creado con su identificador generado
     * @throws PersistenciaException si ocurre un error durante la operación
     */
    Carrito crearCarritoExpress(Carrito carrito) throws PersistenciaException;

    /**
     * Obtiene el carrito express activo utilizando un token único.
     *
     * Este método permite identificar y recuperar un carrito express activo
     * mediante su token de sesión.
     *
     * @param token token único que identifica el carrito express
     * @return el carrito express activo o null si no existe
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    Carrito obtenerCarritoActivoExpress(String token) throws PersistenciaException;
}