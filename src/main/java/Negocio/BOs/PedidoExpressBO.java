package Negocio.BOs;

import Negocio.DTOs.PedidoExpressDTO;
import Negocio.excepciones.NegocioException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;
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

        if (pedto.getEstadoActual() != null) {
            pedto.setEstadoActual(pedto.getEstadoActual().trim());
        }

        if (pedto.getFolio() != null) {
            pedto.setFolio(pedto.getFolio().trim());
        }

        if (pedto.getPIN() != null) {
            pedto.setPIN(pedto.getPIN().trim());
        }

        if (pedto.getNota() != null) {
            pedto.setNota(pedto.getNota().trim());
        }

        if (pedto.getEstadoActual() == null || pedto.getEstadoActual().isBlank()) {
            throw new NegocioException("El estado del pedido es obligatorio.");
        }

        if (pedto.getFechaHoraPedido() == null) {
            throw new NegocioException("La fecha del pedido es obligatoria.");
        }

        if (pedto.getFechaHoraPedido().isAfter(LocalDateTime.now())) {
            throw new NegocioException("La fecha del pedido no puede ser futura.");
        }

        if (pedto.getFechaHoraEntrega() == null) {
            throw new NegocioException("La fecha de entrega es obligatoria.");
        }

        if (pedto.getFechaHoraEntrega().isBefore(pedto.getFechaHoraPedido())) {
            throw new NegocioException("La fecha de entrega no puede ser anterior a la fecha del pedido.");
        }

        String regex = "^-?\\d+$";
        if (pedto.getFolio() == null || !pedto.getFolio().matches(regex)) {
            throw new NegocioException("El folio no es válido");
        }

        if (pedto.getPIN() == null || !pedto.getPIN().matches(regex)) {
            throw new NegocioException("El PIN no es válido");
        }

        PedidoExpress pedidoEx = new PedidoExpress();
        pedidoEx.setFolio(pedto.getFolio());
        pedidoEx.setPin(pedto.getPIN());
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
            pedidoExAgregado.setFolio(pedidoExpressObtenido.getFolio());
            pedidoExAgregado.setPIN(pedidoExpressObtenido.getPin());
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
            pedidoExConsultado.setFolio(pedidoExpressConsultado.getFolio());
            pedidoExConsultado.setPIN(pedidoExpressConsultado.getPin());

            LOG.info(() -> "Se obtuvo el pedidoExpress: " + pedidoExpressConsultado.toString());
            return pedidoExConsultado;

        } catch (PersistenciaException e) {
            LOG.severe(e.getMessage());
            throw new NegocioException(e.getMessage());
        }
    }

    /**
     * Método pendiente para actualizar un PedidoExpress.
     *
     * Actualmente no está implementado.
     *
     * @param pedto DTO con la información a actualizar
     * @return PedidoExpressDTO actualizado
     * @throws NegocioException cuando se implemente la lógica correspondiente
     */
    @Override
    public PedidoExpressDTO actualizarPedidoExpress(PedidoExpressDTO pedto) throws NegocioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
