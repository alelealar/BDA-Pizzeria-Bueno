package Negocio.BOs;

import Negocio.DTOs.CuponDTO;
import Negocio.excepciones.NegocioException;
import persistencia.daos.ICuponDAO;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 * Clase de lógica de negocio encargada de la validación de cupones.
 *
 * <p>
 * Esta clase pertenece a la capa de negocio (BO - Business Object) y se encarga
 * de aplicar validaciones antes de interactuar con la capa de persistencia
 * (DAO).
 * </p>
 *
 * <h2>Responsabilidades:</h2>
 * <ul>
 * <li>Validar que el código del cupón no sea nulo o vacío.</li>
 * <li>Consultar en la base de datos si el cupón existe y está vigente.</li>
 * <li>Convertir la entidad Cupon a CuponDTO.</li>
 * <li>Manejar excepciones provenientes de la capa de persistencia.</li>
 * </ul>
 *
 * <p>
 * Esta clase actúa como intermediaria entre la capa de presentación y la capa
 * de persistencia, asegurando que se cumplan las reglas de negocio antes de
 * devolver la información al usuario.
 * </p>
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public class CuponBO implements ICuponBO {

    private final ICuponDAO cuponDAO;

    /**
     * Constructor que recibe la dependencia del DAO.
     *
     * @param cuponDAO Implementación de ICuponDAO para acceso a datos.
     */
    public CuponBO(ICuponDAO cuponDAO) {
        this.cuponDAO = cuponDAO;
    }

    /**
     * Valida un cupón a partir de su código.
     *
     * <p>
     * Realiza las siguientes validaciones:
     * <ul>
     * <li>El código no debe ser nulo ni estar vacío.</li>
     * <li>El cupón debe existir en la base de datos.</li>
     * <li>El cupón debe estar vigente y disponible.</li>
     * </ul>
     * </p>
     *
     * @param codigo Código del cupón ingresado por el usuario.
     * @return CuponDTO con la información del cupón válido.
     * @throws NegocioException Si el código es inválido, el cupón no existe,
     * está vencido o ocurre un error en persistencia.
     */
    @Override
    public CuponDTO validarCupon(String codigo) throws NegocioException {

        if (codigo == null || codigo.isBlank()) {
            throw new NegocioException("Ingrese un cupón válido");
        }

        Cupon cupon;

        try {
            cupon = cuponDAO.validarCupon(codigo);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al validar cupón", ex);
        }

        if (cupon == null) {
            throw new NegocioException("Cupón inválido o vencido");
        }

        // Convertimos a DTO
        CuponDTO dto = new CuponDTO();
        dto.setIdCupon(cupon.getIdCupon());
        dto.setPorcentajeDescuento(cupon.getPorcentajeDescuento());
        dto.setVigencia(cupon.getVigencia());
        dto.setCantidadUsos(cupon.getCantidadUsos());

        return dto;
    }
}
