/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PedidoProgramadoDTO;
import Negocio.excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.IPedidoProgramadoDAO;
import persistencia.dominio.Cliente;
import persistencia.dominio.Cupon;
import persistencia.dominio.Pedido.Tipo;
import persistencia.dominio.PedidoProgramado;
import persistencia.excepciones.PersistenciaException;
import persistencia.fabrica.FabricaDAO;

/**
 *
 * @author RAYMUNDO
 */
public class PedidoProgramadoBO implements IPedidoProgramadoBO {

    private final IPedidoProgramadoDAO pedidoProgramadoDAO;

    public PedidoProgramadoBO(IPedidoProgramadoDAO pedidoProgramadoDAO) {
        this.pedidoProgramadoDAO = pedidoProgramadoDAO;
    }

    public PedidoProgramadoBO() {
        this.pedidoProgramadoDAO = FabricaDAO.crearPedidoProgramadoDAO();
    }

    
    
    @Override
    public PedidoProgramadoDTO agregarPedidoProgramado(PedidoProgramadoDTO dto)
            throws NegocioException {

        if (dto == null) {
            throw new NegocioException("El pedido está vacío");
        }

        if (dto.getIdCliente() <= 0) {
            throw new NegocioException("Cliente inválido");
        }

        if (dto.getFechaHoraEntrega() == null) {
            throw new NegocioException("Debe indicar fecha y hora de entrega");
        }

        try {

            PedidoProgramado pedido = new PedidoProgramado();

            pedido.setNota(dto.getNota());
            pedido.setFechaHoraEntrega(dto.getFechaHoraEntrega());

            // Cliente
            Cliente cliente = new Cliente();
            cliente.setIdUsuario(dto.getIdCliente());
            pedido.setCliente(cliente);

            // Cupón (si existe)
            if (dto.getIdCupon() != null) {
                Cupon cupon = new Cupon();
                cupon.setIdCupon(String.valueOf(dto.getIdCupon()));
                pedido.setCupon(cupon);
            }

            // Llamar DAO
            PedidoProgramado resultado = pedidoProgramadoDAO.agregarPedidoProgramado(pedido);

            // Convertir a DTO
            PedidoProgramadoDTO respuesta = new PedidoProgramadoDTO();
            respuesta.setIdPedido(resultado.getIdPedido());
            respuesta.setNota(resultado.getNota());
            respuesta.setIdCliente(dto.getIdCliente());
            respuesta.setIdCupon(dto.getIdCupon());
            respuesta.setFechaHoraEntrega(resultado.getFechaHoraEntrega());

            return respuesta;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar pedido programado", e);
        }
    }

    public List<PedidoProgramadoDTO> obtenerPedidosProgramados() throws PersistenciaException {
        List<PedidoProgramado> pedidos = pedidoProgramadoDAO.obtenerPedidosProgramados();
        List<PedidoProgramadoDTO> dtos = new ArrayList<>();

        for (PedidoProgramado p : pedidos) {
            PedidoProgramadoDTO dto = new PedidoProgramadoDTO();
            dto.setIdPedido(p.getIdPedido());
            dto.setNota(p.getNota());
            dto.setEstadoActual(p.getEstadoActual());
            dto.setFechaHoraPedido(p.getFechaHoraPedido());
            dto.setNombreCliente(p.getCliente().getNombres());
            dto.setIdCliente(p.getCliente().getIdUsuario());
            dtos.add(dto);
        }

        return dtos;
    }

    public double calcularTotalPedidoProgramado(int idPedido) throws PersistenciaException {
        return pedidoProgramadoDAO.calcularTotalProgramado(idPedido);
    }

    @Override
    public void agregarDetallePedido(int idPedido, int idPizza, int cantidad, String nota) throws NegocioException {
        try {
            pedidoProgramadoDAO.insertarDetalle(idPedido, idPizza, cantidad, nota);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al agregar detalle pedido programado", e);
        }
    }
}