package Negocio.BOs;

import Negocio.DTOs.CuponDTO;
import Negocio.excepciones.NegocioException;

/**
 * Interfaz que define las operaciones de negocio relacionadas con la validación
 * y gestión de cupones de descuento.
 *
 * <p>
 * Esta interfaz pertenece a la capa de negocio (BO - Business Object) y
 * establece los métodos necesarios para:
 * </p>
 * <ul>
 * <li>Verificar la existencia de códigos promocionales.</li>
 * <li>Validar la vigencia y restricciones de los cupones.</li>
 * <li>Recuperar el porcentaje o monto de descuento aplicable.</li>
 * </ul>
 *
 * <p>
 * Las implementaciones de esta interfaz deberán encargarse de validar la lógica
 * del sistema (fechas, estados de activación) antes de comunicarse con la capa
 * de persistencia.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez
 * @author Alejandra Leal Armenta
 * @author Paulina Michel Guevara Cervantes
 */
public interface ICuponBO {

    /**
     * Valida la existencia y vigencia de un cupón mediante su código.
     *
     * * @param codigo Cadena de texto que representa el código del cupón.
     * @return CuponDTO con la información del descuento si es válido.
     * @throws NegocioException Si el cupón ha expirado, no existe o ya fue
     * utilizado.
     */
    public CuponDTO validarCupon(String codigo) throws NegocioException;

}
