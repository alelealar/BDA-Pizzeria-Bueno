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

    private static String tokenExpress;

    public static String getToken() {
        if (tokenExpress == null) {
            tokenExpress = java.util.UUID.randomUUID().toString();
        }
        return tokenExpress;
    }
}
