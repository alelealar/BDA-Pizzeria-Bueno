package Negocio.BOs;

import Negocio.excepciones.NegocioException;
import persistencia.daos.IPedidoDAO;
import persistencia.dominio.Pedido;
import persistencia.excepciones.PersistenciaException;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PedidoBO implements IPedidoBO {

    private final IPedidoDAO pedidoDAO;
    private final Logger LOG = Logger.getLogger(PedidoBO.class.getName());

    public PedidoBO(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public Pedido agregarPedido(Pedido pedido) throws NegocioException {

        if (pedido == null) {
            throw new NegocioException("El pedido no puede ser nulo.");
        }

        // ⚠ Si id es int, lo normal es que venga en 0 cuando es nuevo
        if (pedido.getIdPedido() > 0) {
            throw new NegocioException("El pedido no debe tener un id asignado.");
        }

        // Normalización
        if (pedido.getEstadoActual() != null) {
            pedido.setEstadoActual(pedido.getEstadoActual().trim());
        }

        if (pedido.getNota() != null) {
            pedido.setNota(pedido.getNota().trim());
        }

        // Validaciones
        if (pedido.getEstadoActual() == null || pedido.getEstadoActual().isBlank()) {
            throw new NegocioException("El estado del pedido es obligatorio.");
        }

        if (pedido.getFechaHoraPedido() == null) {
            throw new NegocioException("La fecha del pedido es obligatoria.");
        }

        if (pedido.getFechaHoraPedido().isAfter(LocalDate.now())) {
            throw new NegocioException("La fecha del pedido no puede ser futura.");
        }

        if (pedido.getFechaHoraEntrega() == null) {
            throw new NegocioException("La fecha de entrega es obligatoria.");
        }

        if (pedido.getFechaHoraEntrega().isBefore(pedido.getFechaHoraPedido())) {
            throw new NegocioException("La fecha de entrega no puede ser anterior a la fecha del pedido.");
        }

        try {
            return pedidoDAO.agregarPedido(pedido);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public Pedido actualizarPedido(Pedido pedido) throws NegocioException {

        if (pedido == null) {
            throw new NegocioException("El pedido no puede ser nulo.");
        }

        if (pedido.getIdPedido() <= 0) {
            throw new NegocioException("El id del pedido debe ser mayor a 0.");
        }

        // Verificamos que exista
        buscarPedidoPorId(pedido.getIdPedido());

        // Normalización
        if (pedido.getEstadoActual() != null) {
            pedido.setEstadoActual(pedido.getEstadoActual().trim());
        }

        if (pedido.getNota() != null) {
            pedido.setNota(pedido.getNota().trim());
        }

        if (pedido.getEstadoActual() == null || pedido.getEstadoActual().isBlank()) {
            throw new NegocioException("El estado del pedido es obligatorio.");
        }

        if (pedido.getFechaHoraPedido() == null || pedido.getFechaHoraEntrega() == null) {
            throw new NegocioException("Las fechas del pedido son obligatorias.");
        }

        if (pedido.getFechaHoraEntrega().isBefore(pedido.getFechaHoraPedido())) {
            throw new NegocioException("La fecha de entrega no puede ser anterior a la fecha del pedido.");
        }

        try {
            return pedidoDAO.actualizarPedido(pedido);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @Override
    public Pedido buscarPedidoPorId(int id) throws NegocioException {

        if (id <= 0) {
            throw new NegocioException("El id debe ser mayor a 0.");
        }

        try {
            return pedidoDAO.buscarPedidoPorId(id);

        } catch (PersistenciaException e) {
            throw new NegocioException(e.getMessage());
        }
    }
}
