package Negocio.BOs;

import Negocio.DTOs.ClienteDTO;
import Negocio.DTOs.TelefonoDTO;
import Negocio.excepciones.NegocioException;
import java.util.List;
import persistencia.dominio.Cliente;
import persistencia.dominio.Telefono;

/**
 * Interfaz que define las operaciones de negocio relacionadas
 * con la gestión de clientes dentro del sistema.
 *
 * <p>
 * Esta interfaz pertenece a la capa de negocio (BO - Business Object)
 * y establece los métodos necesarios para el registro, actualización
 * y administración de información del cliente y sus teléfonos.
 * </p>
 *
 * <h2>Responsabilidades:</h2>
 * <ul>
 *     <li>Registrar nuevos clientes junto con su información de acceso.</li>
 *     <li>Actualizar datos personales del cliente.</li>
 *     <li>Agregar y eliminar teléfonos asociados.</li>
 *     <li>Gestionar el cliente activo en sesión.</li>
 * </ul>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantes - 00000262724
 */
public interface IClienteBO {

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param cliente Información general del cliente.
     * @param usuario Nombre de usuario para el acceso.
     * @param contrasena Contraseña asociada a la cuenta.
     * @param telefonos Lista de teléfonos del cliente.
     * @return ClienteDTO registrado con su información actualizada.
     * @throws NegocioException Si ocurre un error durante el registro.
     */
    ClienteDTO registrarCliente(ClienteDTO cliente, String usuario, String contrasena, List<TelefonoDTO> telefonos) throws NegocioException;
    
    /**
     * Actualiza la información de un cliente existente.
     *
     * @param cliente Datos actualizados del cliente.
     * @return ClienteDTO con la información modificada.
     * @throws NegocioException Si ocurre un error durante la actualización.
     */
    ClienteDTO actualizarCliente(ClienteDTO cliente) throws NegocioException;
    
    /**
     * Agrega un nuevo teléfono a un cliente.
     *
     * @param idCliente Identificador del cliente.
     * @param telefono Objeto teléfono a registrar.
     * @throws NegocioException Si ocurre un error en la operación.
     */
    public void agregarTelefono(int idCliente, Telefono telefono) throws NegocioException;
    
    /**
     * Elimina un teléfono asociado a un cliente.
     *
     * @param idCliente Identificador del cliente.
     * @param telefono Objeto teléfono a eliminar.
     * @throws NegocioException Si ocurre un error en la operación.
     */
    public void eliminarTelefono(int idCliente, Telefono telefono) throws NegocioException;
    
    /**
     * Obtiene el cliente actualmente activo en el sistema.
     *
     * @return ClienteDTO correspondiente al cliente en sesión.
     */
    ClienteDTO getCliente();
    
    /**
     * Establece el cliente activo en el sistema.
     *
     * @param cliente ClienteDTO que se establecerá como activo.
     */
    void setCliente(ClienteDTO cliente);
    
}