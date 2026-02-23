/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persitencia.fabrica;

import Negocio.BOs.ClienteBO;
import Negocio.BOs.UsuarioBO;
import persistencia.DAOS.ClienteDAO;
import persistencia.DAOS.IClienteDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.daos.IPedidoDAO;
import persistencia.daos.IUsuarioDAO;
import persistencia.daos.PedidoDAO;
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

    public static IClienteDAO obtenerClienteDAO() {
        IClienteDAO clienteDAO = new ClienteDAO(conexion);
        return clienteDAO;
    }
    
    public static UsuarioBO getUsuarioBO() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        return new UsuarioBO(usuarioDAO);
    }
    
    public static ClienteBO getClienteBO(){
        ClienteDAO clienteDAO = new ClienteDAO(conexion);
        return new ClienteBO(clienteDAO);
    }
 

}
