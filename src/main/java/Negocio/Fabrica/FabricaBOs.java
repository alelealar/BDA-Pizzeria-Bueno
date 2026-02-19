/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.Fabrica;

import Negocio.BOs.PedidoBO;
import persitencia.fabrica.FabricaDAO;

/**
 *
 * @author Brian
 */
public class FabricaBOs {
    
    public PedidoBO obtenerPedidoBO(){
        PedidoBO pedidoBO = new PedidoBO(FabricaDAO.obtenerPedidoDAO());
        return pedidoBO;
    }
}
