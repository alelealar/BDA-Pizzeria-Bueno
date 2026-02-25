/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.DetallePedidoDTO;
import Negocio.excepciones.NegocioException;
import java.sql.SQLException;
import persistencia.daos.DetallePedidoDAO;

/**
 *
 * @author RAYMUNDO
 */
public class DetallePedidoBO implements IDetallePedidoBO {
    
    private DetallePedidoDAO detallePedidoDAO;

    public DetallePedidoBO(DetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = detallePedidoDAO;
    }

    @Override
    public void agregarDetallePedido(int idPedido, int idPizza, int cantidad, String nota) throws NegocioException {
        try {
            DetallePedidoDTO detalle = new DetallePedidoDTO();
            detalle.setIdPedido(idPedido);
            detalle.setIdPizza(idPizza);
            detalle.setCantidad(cantidad);
            detalle.setNota(nota);
            detallePedidoDAO.insertarDetalle(detalle);
        } catch (SQLException e) {
            throw new NegocioException("No se pudo agregar el detalle del pedido: " + e.getMessage());
        }
    }
}
