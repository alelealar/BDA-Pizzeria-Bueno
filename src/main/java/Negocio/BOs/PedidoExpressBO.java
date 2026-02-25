package Negocio.BOs;

import Negocio.DTOs.PedidoExpressDTO;
import Negocio.excepciones.NegocioException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import persistencia.daos.IPedidoExpressDAO;
import persistencia.dominio.PedidoExpress;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase que contiene la lógica de negocio para PedidoExpress.
 *
 * Aquí se aplican todas las validaciones necesarias antes de enviar la
 * información a la capa de persistencia.
 *
 * La idea es que ningún dato inválido llegue al DAO.
 *
 * @author Brian
 */
public class PedidoExpressBO implements IPedidoExpressBO {

    /**
     * DAO encargado de la comunicación con la base de datos
     */
    IPedidoExpressDAO pedidoExpressDAO;

    /**
     * Logger para registrar advertencias y errores
     */
    private final Logger LOG = Logger.getLogger(PedidoExpressBO.class.getName());

    /**
     * Constructor que recibe el DAO mediante inyección de dependencias. Esto
     * permite mantener separadas la capa de negocio y persistencia.
     *
     * @param pedidoExpressDAO implementación del DAO de PedidoExpress
     */
    public PedidoExpressBO(IPedidoExpressDAO pedidoExpressDAO) {
        this.pedidoExpressDAO = pedidoExpressDAO;
    }

    /**
     * Método que valida y registra un nuevo PedidoExpress.
     *
     * Se encarga de: - Verificar que el objeto no sea nulo. - Validar que el id
     * no venga asignado. - Limpiar espacios en blanco. - Validar fechas. -
     * Validar que folio y PIN sean numéricos. - Enviar la entidad al DAO.
     *
     * @param pedto DTO con la información del pedido express
     * @return PedidoExpressDTO ya registrado con la información generada
     * @throws NegocioException si alguna regla de negocio no se cumple
     */
    @Override
    public PedidoExpressDTO agregarPedidoExpress(PedidoExpressDTO pedto) throws NegocioException {
        if (pedto == null) {
            throw new NegocioException("El pedido no puede ser nulo.");
        }

        if (pedto.getIdPedido() > 0) {
            throw new NegocioException("El Pedido no debe tener un id asignado.");
        }

        PedidoExpress pedidoEx = new PedidoExpress();
        pedidoEx.setFolio(generarFolio());
        String pinPlano = generarPIN();
        pedidoEx.setPin(BCrypt.hashpw(generarPIN(), BCrypt.gensalt()));
        pedidoEx.setNota(pedto.getNota());

        try {
            PedidoExpress pedidoExpressObtenido = pedidoExpressDAO.agregarPedidoExpress(pedidoEx);
            if (pedidoExpressObtenido == null || pedidoExpressObtenido.getIdPedido() <= 0) {
                LOG.warning("Problemas al insertar el pedidoExpress. No se pudo obtener el id.");
                throw new NegocioException("No se inserto el podidoExpress correctamente.");
            }

            PedidoExpressDTO pedidoExAgregado = new PedidoExpressDTO();
            pedidoExAgregado.setEstadoActual(pedidoExpressObtenido.getEstadoActual());
            pedidoExAgregado.setFechaHoraPedido(pedidoExpressObtenido.getFechaHoraPedido());
            pedidoExAgregado.setFolio(String.format("PEDEX-%05d", pedidoExpressObtenido.getFolio()));
            pedidoExAgregado.setPIN(pinPlano);
            return pedidoExAgregado;

        } catch (PersistenciaException e) {
            LOG.severe(e.getMessage());
            throw new NegocioException(e.getMessage());
        }
    }

    /**
     * Método que consulta un PedidoExpress por su id.
     *
     * Primero valida que el id sea mayor a 0. Después consulta en el DAO. Si no
     * existe, lanza excepción.
     *
     * @param idPedidoExpress id del pedido a consultar
     * @return PedidoExpressDTO encontrado
     * @throws NegocioException si el id es inválido o no existe el pedido
     */
    @Override
    public PedidoExpressDTO obtenerPedidoExpressPorId(int idPedidoExpress) throws NegocioException {
        if (idPedidoExpress < 1) {
            LOG.warning("El id no es válido.");
            throw new NegocioException("El id no es válido.");
        }

        try {
            PedidoExpress pedidoExpressConsultado = pedidoExpressDAO.obtenerPedidoExpress(idPedidoExpress);
            if (pedidoExpressConsultado == null) {
                LOG.warning(() -> "No se obtuvo ningún pedido con el id" + idPedidoExpress);
                throw new NegocioException("No se encontró ningún pedidoExpress con el id" + idPedidoExpress);
            }

            PedidoExpressDTO pedidoExConsultado = new PedidoExpressDTO();
            pedidoExConsultado.setEstadoActual(pedidoExpressConsultado.getEstadoActual());
            pedidoExConsultado.setFechaHoraPedido(pedidoExpressConsultado.getFechaHoraPedido());
            pedidoExConsultado.setFolio(String.format("PEDEX-%05d", pedidoExConsultado.getFolio()));
            pedidoExConsultado.setPIN(pedidoExpressConsultado.getPin());

            LOG.info(() -> "Se obtuvo el pedidoExpress: " + pedidoExpressConsultado.toString());
            return pedidoExConsultado;

        } catch (PersistenciaException e) {
            LOG.severe(e.getMessage());
            throw new NegocioException(e.getMessage());
        }
    }

    public String generarPIN() throws NegocioException {
        SecureRandom random = new SecureRandom();
        int digitos = 10000000 + random.nextInt(90000000);
        return String.valueOf(digitos);
    }

    public int generarFolio() throws NegocioException {
        try {
            int ultimo = pedidoExpressDAO.obtenerFolio();
            return ultimo + 1;
        } catch (PersistenciaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @Override
    public String generarToken() throws NegocioException {
        String tokenSesion = UUID.randomUUID().toString();
        return tokenSesion;

    }
}
