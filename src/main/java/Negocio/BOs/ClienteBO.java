package Negocio.BOs;

import Negocio.DTOs.ClienteDTO;
import Negocio.DTOs.TelefonoDTO;
import Negocio.DTOs.UsuarioDTO;
import Negocio.excepciones.NegocioException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.DAOS.IClienteDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Domicilio;
import persistencia.dominio.Telefono;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de la gestión de clientes.
 *
 * <p>
 * Esta clase pertenece a la capa de negocio (BO - Business Object) y se encarga
 * de aplicar las validaciones necesarias antes de interactuar con la capa de
 * persistencia (DAO).
 * </p>
 *
 * <h2>Responsabilidades principales:</h2>
 * <ul>
 * <li>Registrar nuevos clientes.</li>
 * <li>Actualizar información de clientes.</li>
 * <li>Validar datos personales y domicilio.</li>
 * <li>Validar fecha de nacimiento.</li>
 * <li>Gestionar teléfonos asociados al cliente.</li>
 * <li>Convertir objetos DTO a entidades de dominio y viceversa.</li>
 * </ul>
 *
 * <p>
 * Aplica reglas de negocio como: validación de campos obligatorios, validación
 * de fecha de nacimiento, validación de domicilio completo, y control de
 * errores provenientes de la capa de persistencia.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class ClienteBO implements IClienteBO {

    private IClienteDAO clienteDAO;
    private ClienteDTO clienteActual;
    private static final Logger LOG = Logger.getLogger(ClienteBO.class.getName());

    /**
     * Constructor que recibe la dependencia DAO para acceso a datos.
     *
     * @param cliente Implementación de IClienteDAO.
     */
    public ClienteBO(IClienteDAO cliente) {
        this.clienteDAO = cliente;
    }

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * <p>
     * Realiza validaciones de:
     * <ul>
     * <li>Campos obligatorios.</li>
     * <li>Domicilio completo si se empieza a llenar.</li>
     * <li>Fecha de nacimiento válida y no futura.</li>
     * <li>Usuario y contraseña obligatorios.</li>
     * </ul>
     * </p>
     *
     * @param cliente Información del cliente en formato DTO.
     * @param usuario Nombre de usuario para autenticación.
     * @param contrasena Contraseña del usuario.
     * @param telefonos Lista de teléfonos asociados.
     * @return ClienteDTO registrado con información persistida.
     * @throws NegocioException Si alguna validación falla o hay error en
     * persistencia.
     */
    public ClienteDTO registrarCliente(ClienteDTO cliente, String usuario, String contrasena, List<TelefonoDTO> telefonos) throws NegocioException {

        if (cliente == null) {
            LOG.warning("Cliente nulo");
            throw new NegocioException("El cliente no puede ser nulo");
        }

        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            LOG.warning("El campo nombre se dejó vacío");
            throw new NegocioException("El nombre del cliente es obligatorio");
        }

        if (cliente.getCalle() != null && !cliente.getCalle().isBlank()) {
            if (cliente.getNumero() == null || cliente.getNumero().isBlank()
                    || cliente.getColonia() == null || cliente.getColonia().isBlank()
                    || cliente.getCP() == null || cliente.getCP().isBlank()) {
                LOG.warning("Domicilio incompleto");
                throw new NegocioException("Debe proporcionar el domicilio completo si empieza a llenarlo");
            }
        }

        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.of(cliente.getAnio(), cliente.getMesNum(), cliente.getDia());
        } catch (DateTimeException e) {
            LOG.warning("Fecha de nacimiento inválida");
            throw new NegocioException("La fecha de nacimiento no es válida.");
        }

        if (fechaNacimiento.isAfter(LocalDate.now())) {
            LOG.warning("Fecha de nacimiento futura");
            throw new NegocioException("La fecha de nacimiento no puede ser posterior a hoy.");
        }

        if (usuario.isBlank() || usuario == null) {
            LOG.warning("no se ingreso usuario, error");
            throw new NegocioException("Ingrese un usuario");
        }

        if (contrasena.isBlank() || contrasena == null) {
            LOG.warning("No se ingreso ninguna contraseña");
            throw new NegocioException("Contraseña obligatoria.");
        }

        try {
            Cliente clienteDominio = mapearDTOaDominio(cliente);
            clienteDominio.setNombreUsuario(usuario);
            clienteDominio.setContraseniaUsuario(contrasena);

            Cliente clienteRegistrado = clienteDAO.agregarCliente(clienteDominio);

            if (telefonos != null && !telefonos.isEmpty()) {
                List<Telefono> listaTelefonosDominio = convertirListaTelefonos(telefonos, clienteRegistrado);
                clienteDAO.agregarTelefonos(clienteRegistrado.getIdCliente(), listaTelefonosDominio);
            }

            ClienteDTO resultado = mapearDominioaDTO(clienteRegistrado);
            return resultado;

        } catch (PersistenciaException ex) {
            LOG.severe("Error al insertar al cliente en la BD");
            throw new NegocioException("No se pudo registrar al cliente. Intente más tarde.", ex);
        }
    }

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param cliente ClienteDTO con información actualizada.
     * @return ClienteDTO actualizado.
     * @throws NegocioException Si falla validación o persistencia.
     */
    public ClienteDTO actualizarCliente(ClienteDTO cliente) throws NegocioException {

        if (cliente == null) {
            LOG.warning("Cliente nulo");
            throw new NegocioException("El cliente no puede ser nulo");
        }

        if (cliente.getNombres() == null || cliente.getNombres().isBlank()) {
            LOG.warning("El campo nombre se dejó vacío");
            throw new NegocioException("El nombre del cliente es obligatorio");
        }

        if (cliente.getCalle() != null && !cliente.getCalle().isBlank()) {
            if (cliente.getNumero() == null || cliente.getNumero().isBlank()
                    || cliente.getColonia() == null || cliente.getColonia().isBlank()
                    || cliente.getCP() == null || cliente.getCP().isBlank()) {
                LOG.warning("Domicilio incompleto");
                throw new NegocioException("Debe proporcionar el domicilio completo si empieza a llenarlo");
            }
        }

        LocalDate fechaNac = LocalDate.of(cliente.getAnio(), cliente.getMesNum(), cliente.getDia());

        if (fechaNac.isAfter(LocalDate.now())) {
            LOG.warning("fecha nacimiento invalida. despues de hoy ");
            throw new NegocioException("Debe proporcionar una fecha de nacimiento válida.");
        }

        try {
            Cliente clienteDominio = mapearDTOaDominio(cliente);
            Cliente clienteActualizado = clienteDAO.actualizarCliente(clienteDominio);
            ClienteDTO resultado = mapearDominioaDTO(clienteActualizado);
            return resultado;

        } catch (PersistenciaException ex) {
            LOG.severe("Error al actualizar el cliente en la BD");
            throw new NegocioException("No se pudo actualizar al cliente. Intente más tarde.", ex);
        }
    }

    /**
     * Método pendiente para agregar teléfono a un cliente.
     */
    @Override
    public void agregarTelefono(int idCliente, Telefono telefono) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Método pendiente para eliminar teléfono de un cliente.
     */
    @Override
    public void eliminarTelefono(int idCliente, Telefono telefono) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Convierte un ClienteDTO en entidad de dominio Cliente.
     */
    private Cliente mapearDTOaDominio(ClienteDTO dto) {

        Cliente dominio = new Cliente();
        dominio.setNombres(dto.getNombres());

        String apellidosCompletos = dto.getApellidos();

        if (apellidosCompletos != null && !apellidosCompletos.isBlank()) {
            String[] partes = apellidosCompletos.split(" ");

            if (partes.length >= 2) {
                dominio.setApellidoPaterno(partes[0]);
                dominio.setApellidoMaterno(partes[1]);
            } else {
                dominio.setApellidoPaterno(partes[0]);
                dominio.setApellidoMaterno(null);
            }
        }

        dominio.setFechaNacimiento(LocalDate.of(dto.getAnio(), dto.getMesNum(), dto.getDia()));

        Domicilio dom = new Domicilio();
        dom.setCalle(dto.getCalle());
        dom.setNumero(dto.getNumero());
        dom.setColonia(dto.getColonia());
        dom.setCodigoPostal(dto.getCP());
        dominio.setDomicilio(dom);

        return dominio;
    }

    /**
     * Convierte una entidad Cliente en ClienteDTO.
     */
    private ClienteDTO mapearDominioaDTO(Cliente cliente) {

        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setNombres(cliente.getNombres());
        dto.setApellidoPaterno(cliente.getApellidoPaterno());
        cliente.setFechaNacimiento(cliente.getFechaNacimiento());

        Domicilio domicilio = cliente.getDomicilio();
        if (domicilio != null) {
            dto.setCalle(domicilio.getCalle());
            dto.setNumero(domicilio.getNumero());
            dto.setColonia(domicilio.getColonia());
            dto.setCP(domicilio.getCodigoPostal());
        }

        return dto;
    }

    /**
     * Convierte una lista de TelefonoDTO a entidades Telefono asociadas a un
     * cliente.
     */
    private List<Telefono> convertirListaTelefonos(List<TelefonoDTO> listaDTO, Cliente cliente) {

        List<Telefono> listaDominio = new ArrayList<>();

        for (TelefonoDTO dto : listaDTO) {
            Telefono entidad = new Telefono();
            entidad.setEtiqueta(dto.getEtiqueta());
            entidad.setTelefono(dto.getTelefono());
            entidad.setCliente(cliente);
            listaDominio.add(entidad);
        }

        return listaDominio;
    }

    /**
     * Devuelve el cliente actualmente manejado por este BO.
     *
     * @return ClienteDTO actual.
     */
    public ClienteDTO getCliente() {
        return clienteActual;
    }

    /**
     * Permite asignar un cliente al BO.
     *
     * @param cliente ClienteDTO a asignar.
     */
    public void setCliente(ClienteDTO cliente) {
        this.clienteActual = cliente;
    }
}
