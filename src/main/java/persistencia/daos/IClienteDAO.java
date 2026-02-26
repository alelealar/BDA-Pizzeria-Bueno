package persistencia.DAOS;

import java.util.List;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link Cliente}.
 * 
 * Esta interfaz forma parte del patrón DAO (Data Access Object), cuyo propósito es abstraer
 * el acceso a la base de datos y proporcionar métodos para registrar, actualizar y consultar
 * clientes almacenados en el sistema.
 * 
 * También permite gestionar la información asociada a los teléfonos de los clientes.
 * 
 * Las clases que implementen esta interfaz serán responsables de ejecutar las operaciones
 * necesarias para interactuar con la base de datos.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IClienteDAO {

    /**
     * Registra un nuevo cliente en la base de datos.
     * 
     * El objeto {@link Cliente} debe contener toda la información necesaria
     * para su almacenamiento.
     * 
     * @param cliente objeto Cliente con los datos a registrar
     * @return objeto Cliente registrado con su identificador generado
     * @throws PersistenciaException si ocurre un error durante el registro
     */
    Cliente agregarCliente(Cliente cliente) throws PersistenciaException;

    /**
     * Registra los teléfonos asociados a un cliente en la base de datos.
     * 
     * Este método permite almacenar uno o varios números telefónicos
     * relacionados con un cliente existente.
     * 
     * @param idCliente identificador del cliente al que pertenecen los teléfonos
     * @param telefonos lista de objetos {@link Telefono} a registrar
     * @throws PersistenciaException si ocurre un error durante el registro
     */
    void agregarTelefonos(int idCliente, List<Telefono> telefonos) throws PersistenciaException;

    /**
     * Actualiza la información de un cliente existente en la base de datos.
     * 
     * El objeto {@link Cliente} debe contener un identificador válido y los
     * nuevos valores que se desean actualizar.
     * 
     * @param cliente objeto Cliente con los nuevos datos
     * @return objeto Cliente actualizado
     * @throws PersistenciaException si el cliente no existe o ocurre un error durante la actualización
     */
    Cliente actualizarCliente(Cliente cliente) throws PersistenciaException;

    /**
     * Busca un cliente en la base de datos utilizando su identificador único.
     * 
     * @param id identificador del cliente a buscar
     * @return objeto Cliente encontrado o null si no existe
     * @throws PersistenciaException si ocurre un error durante la búsqueda
     */
    Cliente buscarClientePorId(int id) throws PersistenciaException;

    /**
     * Obtiene todos los clientes registrados en la base de datos.
     * 
     * @return lista de objetos {@link Cliente} registrados
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    List<Cliente> consultarClientes() throws PersistenciaException;

    /**
     * Busca un cliente utilizando un número de teléfono asociado.
     * 
     * Este método permite identificar un cliente a partir de uno de sus
     * teléfonos registrados.
     * 
     * @param telefono número telefónico del cliente
     * @return objeto Cliente encontrado o null si no existe
     * @throws PersistenciaException si ocurre un error durante la búsqueda
     */
    Cliente buscarClientePorTelefono(String telefono) throws PersistenciaException;

}