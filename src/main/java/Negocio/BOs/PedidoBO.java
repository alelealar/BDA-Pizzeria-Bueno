package Negocio.BOs;

import Negocio.DTOs.PedidoDTO;
import Negocio.DTOs.PedidoDetalleDTO;
import Negocio.DTOs.PedidoTablaDTO;
import Negocio.excepciones.NegocioException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.IPedidoDAO;
import persistencia.dominio.Pedido;
import persistencia.dominio.PedidoResumen;
import persistencia.excepciones.PersistenciaException;

public class PedidoBO implements IPedidoBO {

    private final IPedidoDAO pedidoDAO;

    public PedidoBO(IPedidoDAO pedidoDAO) {
        this.pedidoDAO = pedidoDAO;
    }

    @Override
    public PedidoDTO agregarPedido(PedidoDTO pedidoDTO) throws NegocioException {
        try {

            validarPedido(pedidoDTO);

            Pedido pedido = convertirEntidad(pedidoDTO);

            Pedido pedidoGuardado = pedidoDAO.agregarPedido(pedido);

            return convertirDTO(pedidoGuardado);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al agregar el pedido", ex);
        }
    }

    @Override
    public PedidoDTO actualizarPedido(PedidoDTO pedidoDTO) throws NegocioException {
        try {

            validarPedido(pedidoDTO);

            Pedido pedido = convertirEntidad(pedidoDTO);

            Pedido pedidoActualizado = pedidoDAO.actualizarPedido(pedido);

            return convertirDTO(pedidoActualizado);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al actualizar el pedido", ex);
        }
    }

    @Override
    public PedidoDTO buscarPedidoPorId(int id) throws NegocioException {
        try {

            Pedido pedido = pedidoDAO.buscarPedidoPorId(id);

            return convertirDTO(pedido);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al buscar el pedido", ex);
        }
    }

    @Override
    public List<PedidoDTO> obtenerPedidos() throws NegocioException {
        try {

            List<Pedido> pedidos = pedidoDAO.obtenerPedidos();
            List<PedidoDTO> pedidosDTO = new ArrayList<>();

            for (Pedido p : pedidos) {
                pedidosDTO.add(convertirDTO(p));
            }

            return pedidosDTO;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener los pedidos", ex);
        }
    }

    @Override
    public List<PedidoDTO> consultarPorRangoDeFechas(LocalDate inicio, LocalDate fin)
            throws NegocioException {

        try {

            List<Pedido> pedidos = pedidoDAO.consultarPorRangoDeFechas(inicio, fin);
            List<PedidoDTO> pedidosDTO = new ArrayList<>();

            for (Pedido p : pedidos) {
                pedidosDTO.add(convertirDTO(p));
            }

            return pedidosDTO;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar pedidos por rango", ex);
        }
    }

    private void validarPedido(PedidoDTO pedido) throws NegocioException {

        if (pedido.getNota() == null || pedido.getNota().isBlank()) {
            throw new NegocioException("La nota no puede estar vacía.");
        }

        if (pedido.getEstadoActual() == null || pedido.getEstadoActual().isBlank()) {
            throw new NegocioException("El estado no puede estar vacío.");
        }

        if (pedido.getFechaHoraPedido() == null) {
            throw new NegocioException("La fecha del pedido es obligatoria.");
        }

        if (pedido.getFechaHoraEntrega() == null) {
            throw new NegocioException("La fecha de entrega es obligatoria.");
        }
    }

    private Pedido convertirEntidad(PedidoDTO dto) {

        Pedido pedido = new Pedido();

        pedido.setIdPedido(dto.getIdPedido());
        pedido.setNota(dto.getNota());
        pedido.setEstadoActual(dto.getEstadoActual());
        pedido.setFechaHoraPedido(dto.getFechaHoraPedido().atStartOfDay());
        pedido.setFechaHoraEntrega(dto.getFechaHoraEntrega().atStartOfDay());
        pedido.setTipo(dto.getTipo() == PedidoDTO.Tipo.PROGRAMADO
                ? Pedido.Tipo.PROGRAMADO
                : Pedido.Tipo.EXPRESS);

        return pedido;
    }

    private PedidoDTO convertirDTO(Pedido pedido) {

        PedidoDTO dto = new PedidoDTO();

        dto.setIdPedido(pedido.getIdPedido());
        dto.setNota(pedido.getNota());
        dto.setEstadoActual(pedido.getEstadoActual());
        dto.setFechaHoraPedido(pedido.getFechaHoraPedido().toLocalDate());
        dto.setFechaHoraEntrega(pedido.getFechaHoraEntrega().toLocalDate());
        dto.setTipo(pedido.getTipo() == Pedido.Tipo.PROGRAMADO
                ? PedidoDTO.Tipo.PROGRAMADO
                : PedidoDTO.Tipo.EXPRESS);

        return dto;
    }

    @Override
    public List<PedidoTablaDTO> obtenerPedidosTabla() throws NegocioException {
        try {
            List<PedidoResumen> listaResumen = pedidoDAO.obtenerPedidosTabla();
            List<PedidoTablaDTO> listaDTO = new ArrayList<>();

            for (PedidoResumen r : listaResumen) {
                PedidoTablaDTO dto = new PedidoTablaDTO(
                        r.getIdPedido(),
                        r.getFolio(),
                        r.getCliente(),
                        r.getTelefono(),
                        r.getFechaHora(),
                        r.getTotal(),
                        r.getEstado(),
                        r.getTipo()
                );
                listaDTO.add(dto);
            }

            return listaDTO;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener pedidos para la tabla", e);
        }
    }

    @Override
    public List<PedidoTablaDTO> obtenerPedidosFiltrados(String filtro) throws NegocioException {
        try {

            List<PedidoResumen> listaDAO = pedidoDAO.obtenerPedidosFiltrados(filtro);
            List<PedidoTablaDTO> listaDTO = new ArrayList<>();

            for (PedidoResumen p : listaDAO) {
                PedidoTablaDTO dto = new PedidoTablaDTO(
                        p.getIdPedido(),
                        p.getFolio(),
                        p.getCliente(),
                        p.getTelefono(),
                        p.getFechaHora(),
                        p.getTotal(),
                        p.getEstado(),
                        p.getTipo()
                );

                listaDTO.add(dto);
            }

            return listaDTO;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al filtrar pedidos", ex);
        }
    }

//    @Override
//    public void cambiarEstado(int idPedido, String nuevoEstado) throws NegocioException {
//        try {
//            pedidoDAO.cambiarEstado(idPedido, nuevoEstado);
//        } catch (PersistenciaException ex) {
//            throw new NegocioException("Error al cambiar estado de pedido", ex);
//        }
//    }

    @Override
    public void cambiarEstado(int idPedido, String nuevoEstado)
            throws NegocioException {

        try {

            pedidoDAO.cambiarEstado(idPedido, nuevoEstado);

        } catch (PersistenciaException e) {

            throw new NegocioException(
                    e.getMessage(),
                    e
            );

        }

    }

    @Override
    public PedidoDetalleDTO obtenerDetallePedido(int idPedido) throws NegocioException {

        if (idPedido <= 0) {
            throw new NegocioException("ID de pedido inválido.");
        }

        try {

            PedidoDetalleDTO pedido = pedidoDAO.obtenerDetallePedido(idPedido);

            if (pedido == null) {
                throw new NegocioException("No se encontró el pedido.");
            }

            return pedido;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener detalle del pedido: "
                    + e.getMessage());
        }
    }

    @Override
    public boolean validarFolioYPIN(int idPedido, String folio, String pin) throws NegocioException {
        try {
            return pedidoDAO.validarFolioYPIN(idPedido, folio, pin);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al validar folio y pin: "
                    + ex.getMessage());
        }
    }
}
