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
import persistencia.daos.ITelefonoDAO;
import persistencia.daos.IUsuarioDAO;
import persistencia.daos.PedidoDAO;
import persistencia.daos.PedidoExpressDAO;
import persistencia.daos.PizzaDAO;
import persistencia.daos.TelefonoDAO;
import persistencia.daos.UsuarioDAO;

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

    public static IUsuarioDAO obtenerUsuarioDAO(){
        IUsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        return usuarioDAO;
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

    
    public static ITelefonoDAO obtenerTelefonoDAO(){
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        ITelefonoDAO telefonoDAO = new TelefonoDAO(conexion, clienteDAO);
        return telefonoDAO;
    }
}
