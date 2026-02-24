package persistencia.fabrica;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import persistencia.DAOS.ClienteDAO;
import persistencia.DAOS.IClienteDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.daos.IPedidoDAO;
import persistencia.daos.IPedidoExpressDAO;
import persistencia.daos.IPizzaDAO;
import persistencia.daos.PedidoDAO;
import persistencia.daos.PedidoExpressDAO;
import persistencia.daos.PizzaDAO;

/**
 *
 * @author Brian
 */
public class FabricaDAO {

    private static IConexionBD conexion = new ConexionBD();

    public static IPedidoDAO obtenerPedidoDAO() {

        IPedidoDAO pedidoDAO = new PedidoDAO(conexion);
        return pedidoDAO;
    }

    public static IClienteDAO obtenerClienteDAO() {
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        return clienteDAO;
    }

    public static IPizzaDAO obtenerPizzaDAO() {
        IPizzaDAO pizzaDAO = new PizzaDAO(conexion);
        return pizzaDAO;
    }

    public static IPedidoExpressDAO obtenerPedidoExpressDAO() {
        IPedidoExpressDAO pedidoExpressDAO = new PedidoExpressDAO(conexion);
        return pedidoExpressDAO;
    }
    
    public static IPedidoDAO crearPedidoDAO() {
        return new PedidoDAO(conexion);
    }

}
