
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.Fabrica;

import Negocio.BOs.ClienteBO;
import Negocio.BOs.IPedidoBO;
import Negocio.BOs.PedidoBO;
import Negocio.BOs.PedidoExpressBO;
import Negocio.BOs.PizzaBO;
import Negocio.BOs.UsuarioBO;
import Negocio.BOs.TelefonoBO;
import persistencia.fabrica.FabricaDAO;

/**
 *
 * @author Brian
 */
public class FabricaBOs {

//    public PedidoBO obtenerPedidoBO() {
//        PedidoBO pedidoBO = new PedidoBO(FabricaDAO.obtenerPedidoDAO());
//        return pedidoBO;
//    }
    public static PizzaBO obtenerProductos() {
        PizzaBO pizzaBO = new PizzaBO(FabricaDAO.obtenerPizzaDAO());
        return pizzaBO;
    }

    public static PedidoExpressBO obtenerPedidoExpress() {
        PedidoExpressBO pedidoExBO = new PedidoExpressBO(FabricaDAO.obtenerPedidoExpressDAO());
        return pedidoExBO;
    }

    public static UsuarioBO obtenerUsuario() {
        UsuarioBO usuarioBO = new UsuarioBO(FabricaDAO.obtenerUsuarioDAO());
        return usuarioBO;
    }

    public static ClienteBO obtenerCliente() {
        ClienteBO pedidoExBO = new ClienteBO(FabricaDAO.obtenerClienteDAO());
        return pedidoExBO;
    }

    public static TelefonoBO obtenerTelefono() {
        TelefonoBO telefonoBO = new TelefonoBO(FabricaDAO.obtenerTelefonoDAO());
        return telefonoBO;
    }

    public static IPedidoBO crearPedidoBO() {
        return new PedidoBO(FabricaDAO.crearPedidoDAO());
    }

}
