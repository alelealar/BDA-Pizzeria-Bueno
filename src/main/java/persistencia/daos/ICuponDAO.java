package persistencia.daos;

import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link Cupon}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object) y proporciona los métodos
 * necesarios para consultar, validar y actualizar la información de los cupones almacenados
 * en la base de datos.
 * 
 * Permite gestionar la obtención de cupones por su identificador, validar su vigencia y
 * disponibilidad, así como registrar el uso de un cupón cuando es aplicado en un pedido.
 * 
 * Las clases que implementen esta interfaz serán responsables de interactuar directamente
 * con la base de datos para realizar estas operaciones.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface ICuponDAO {

    /**
     * Obtiene un cupón de la base de datos utilizando su identificador único.
     * 
     * Este método permite recuperar toda la información asociada a un cupón,
     * como su porcentaje de descuento, vigencia y cantidad de usos.
     * 
     * @param id identificador único del cupón
     * @return objeto {@link Cupon} correspondiente al identificador proporcionado
     * @throws PersistenciaException si el cupón no existe o ocurre un error durante la consulta
     */
    Cupon obtenerCuponPorId(String id) throws PersistenciaException;

    /**
     * Incrementa la cantidad de usos de un cupón en la base de datos.
     * 
     * Este método se utiliza cuando un cupón ha sido aplicado correctamente
     * en un pedido, actualizando el contador de usos.
     * 
     * @param cupon objeto {@link Cupon} cuyo contador de usos será incrementado
     * @return objeto {@link Cupon} actualizado con la nueva cantidad de usos
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    Cupon incrementarUsosCupon(Cupon cupon) throws PersistenciaException;

    /**
     * Valida un cupón en la base de datos utilizando su código.
     * 
     * Este método verifica que el cupón exista, esté vigente y cumpla con
     * las condiciones necesarias para ser utilizado.
     * 
     * @param codigo código del cupón a validar
     * @return objeto {@link Cupon} válido si cumple las condiciones, o null si no es válido
     * @throws PersistenciaException si ocurre un error durante la validación
     */
    Cupon validarCupon(String codigo) throws PersistenciaException;
}