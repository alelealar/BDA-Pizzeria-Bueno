/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package presentacion;

import Negocio.BOs.IPizzaBO;
import Negocio.BOs.PizzaBO;
import javax.swing.SwingUtilities;
import persistencia.conexion.ConexionBD;
import persistencia.conexion.IConexionBD;
import persistencia.daos.IPizzaDAO;
import persistencia.daos.PizzaDAO;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class Inicio {

    /**
     * @param args the command line arguments
     * @throws persistencia.excepciones.PersistenciaException
     */
    public static void main(String[] args) throws PersistenciaException {

        IConexionBD conexion = new ConexionBD();
        IPizzaDAO pizzaDAO = new PizzaDAO(conexion);
        IPizzaBO pizzaBO = new PizzaBO(pizzaDAO);

        new PantallaInicioPedidoProgramado(pizzaBO).setVisible(true);

    }
}
