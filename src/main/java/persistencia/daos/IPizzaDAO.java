package persistencia.daos;

import java.util.List;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 * Interfaz que define las operaciones de persistencia relacionadas con la entidad {@link Pizza}.
 * 
 * Esta interfaz establece el contrato que deben cumplir las clases encargadas
 * de gestionar la información de las pizzas en la base de datos.
 * 
 * Permite realizar consultas, búsquedas, actualizaciones de precios y cambios
 * de estado de las pizzas disponibles en el sistema.
 * 
 * Forma parte del patrón DAO (Data Access Object), facilitando el desacoplamiento
 * entre la lógica de negocio y el acceso a datos.
 * 
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */
public interface IPizzaDAO {

    /**
     * Obtiene la lista de todas las pizzas registradas en la base de datos.
     * 
     * Incluye la información general de cada pizza, como nombre, descripción,
     * tamaños disponibles, precios y estado.
     *
     * @return lista de objetos {@link Pizza}
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    public List<Pizza> obtenerPizzas() throws PersistenciaException;

    /**
     * Obtiene una pizza específica utilizando su identificador único.
     *
     * @param id identificador de la pizza
     * @return objeto {@link Pizza} correspondiente al identificador proporcionado
     * @throws PersistenciaException si no se encuentra la pizza o ocurre un error
     */
    public Pizza obtenerPizzaPorId(int id) throws PersistenciaException;

    /**
     * Obtiene una pizza específica según su nombre y tamaño.
     * 
     * Este método permite recuperar una pizza considerando una de sus variantes
     * de tamaño, ya que el precio puede variar según el tamaño seleccionado.
     *
     * @param nombre nombre de la pizza
     * @param tamPizza tamaño de la pizza (por ejemplo: CHICA, MEDIANA, GRANDE)
     * @return objeto {@link Pizza} con la información correspondiente
     * @throws PersistenciaException si ocurre un error durante la consulta
     */
    Pizza obtenerPizzaPorTamano(String nombre, String tamPizza) throws PersistenciaException;

    /**
     * Actualiza el precio de una pizza específica según su nombre y tamaño.
     *
     * Este método permite modificar el precio de una variante específica
     * de una pizza en la base de datos.
     *
     * @param nombre nombre de la pizza
     * @param tam tamaño de la pizza
     * @param precioNuevo nuevo precio a asignar
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    void actualizarPrecioPizza(String nombre, String tam, double precioNuevo) throws PersistenciaException;

    /**
     * Actualiza el estado de una pizza en la base de datos.
     * 
     * Este método permite cambiar el estado de disponibilidad de la pizza,
     * por ejemplo, marcarla como DISPONIBLE o NO DISPONIBLE.
     *
     * @param idPizza identificador de la pizza
     * @throws PersistenciaException si ocurre un error durante la actualización
     */
    void actualizarEstadoPizza(int idPizza) throws PersistenciaException;
}