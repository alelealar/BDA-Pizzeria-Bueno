/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.sesion;

/**
 * Esta clase evita que mi token se vuelva a generar cada vez que cierro una
 * ventana.
 *
 * @author Brian
 */
public class SesionExpress {

    private static String token;

    public static String getToken() {
        if (token == null) {
            token = java.util.UUID.randomUUID().toString();
        }
        return token;
    }
}