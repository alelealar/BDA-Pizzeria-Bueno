package persistencia.daos;

import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link Telefono}.
 * 
 * Esta interfaz establece el contrato que deben cumplir las clases encargadas
 * de gestionar los teléfonos asociados a los clientes en la base de datos.
 * 
 * Permite realizar operaciones como registrar, eliminar y consultar teléfonos.
 * 
 * Forma parte del patrón DAO (Data Access Object), permitiendo desacoplar
 * la lógica de acceso a datos de la lógica de negocio.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface ITelefonoDAO {

    /**
     * Registra un nuevo teléfono en la base de datos.
     * 
     * El objeto {@link Telefono} debe contener la información necesaria,
     * como el número telefónico, la etiqueta (por ejemplo: casa, móvil, trabajo)
     * y el identificador del cliente asociado.
     *
     * @param telefono objeto {@link Telefono} con la información a registrar
     * @return objeto {@link Telefono} con el identificador generado
     * @throws PersistenciaException si ocurre un error durante el registro
     */
    Telefono agregarTelefono(Telefono telefono) throws PersistenciaException;

    /**
     * Elimina un teléfono existente de la base de datos.
     *
     * @param idTelefono identificador del teléfono a eliminar
     * @return objeto {@link Telefono} eliminado
     * @throws PersistenciaException si el teléfono no existe o ocurre un error
     */
    Telefono eliminarTelefono(int idTelefono) throws PersistenciaException;

    /**
     * Obtiene un teléfono específico utilizando su identificador.
     *
     * @param idTelefono identificador del teléfono
     * @return objeto {@link Telefono} encontrado
     * @throws PersistenciaException si no se encuentra el teléfono o ocurre un error
     */
    Telefono obtenerTelefono(int idTelefono) throws PersistenciaException;
}