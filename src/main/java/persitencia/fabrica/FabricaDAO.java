/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persitencia.fabrica;

import persistencia.DAOS.ClienteDAO;
import persistencia.DAOS.IClienteDAO;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.daos.IPedidoDAO;
import persistencia.daos.PedidoDAO;

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

}
