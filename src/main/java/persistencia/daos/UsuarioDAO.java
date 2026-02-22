/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia.daos;

import Negocio.DTOs.UsuarioDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import persistencia.DAOS.ClienteDAO;
import persistencia.conexion.IConexionBD;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class UsuarioDAO implements IUsuarioDAO {
    /**
     * Componente encargado de crear conexiones con la base de datos.
     *
     * Se inyecta por constructor para reducir acoplamiento y facilitar pruebas.
     */
    private final IConexionBD conexionBD;

    /**
     * Logger para registrar información relevante durante operaciones de
     * persistencia.
     */
    private static final Logger LOG = Logger.getLogger(ClienteDAO.class.getName());

    /**
     * Constructor que inicializa la dependencia de conexión.
     *
     * @param conexionBD objeto que gestiona la creación de conexiones a la base
     * de datos
     */
    public UsuarioDAO(IConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    
    @Override
    public UsuarioDTO iniciarSesion(String usuario, String contraseña) throws PersistenciaException {

        String sql = """
            SELECT idUsuario, usuario, contraseña, rol
            FROM Usuarios
            WHERE usuario = ? AND contraseña = ?
        """;

        try (Connection con = conexionBD.crearConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new UsuarioDTO(
                        rs.getInt("idUsuario"),
                        rs.getString("usuario"),
                        rs.getString("rol")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al iniciar sesión", e);
        }
    }
}
