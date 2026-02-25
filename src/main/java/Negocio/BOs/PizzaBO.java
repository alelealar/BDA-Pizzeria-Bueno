/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.PizzaDTO;
import Negocio.DTOs.PizzaProductoDTO;
import Negocio.excepciones.NegocioException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.daos.IPizzaDAO;
import persistencia.daos.PizzaDAO;
import persistencia.dominio.Pizza;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class PizzaBO implements IPizzaBO {

    private final IPizzaDAO pizzaDAO;
    private final Logger LOG = Logger.getLogger(PizzaBO.class.getName());

    public PizzaBO(IPizzaDAO pizzaDAO) {
        this.pizzaDAO = pizzaDAO;
    }

    public PizzaBO() {
        this.pizzaDAO = new PizzaDAO();
    }

    @Override
    public List<PizzaDTO> obtenerProductos() throws NegocioException {
        List<Pizza> pizzas;
        try {
            pizzas = pizzaDAO.obtenerPizzas();
            List<PizzaDTO> dtos = new ArrayList<>();

            for (Pizza p : pizzas) {
                PizzaDTO dto = new PizzaDTO();
                dto.setIdPizza(p.getIdPizza());
                dto.setNombre(p.getNombre());
                dto.setVariante(p.getTamanio(), p.getPrecio(), p.getIdPizza());
                dto.setDescripcion(p.getDescripcion());
                dto.setRutaImagen(p.getRutaImagen());

                if (p.getEstado() == Pizza.EstadoPizza.DISPONIBLE) {
                    dto.setEstado(PizzaDTO.EstadoPizza.DISPONIBLE);
                } else {
                    dto.setEstado(PizzaDTO.EstadoPizza.NO_DISPONIBLE);
                }

                LOG.info(() -> "Se obtuvo la lista: " + dto.toString());
                dtos.add(dto);

            }
            return dtos;
        } catch (PersistenciaException ex) {
            Logger.getLogger(PizzaBO.class.getName()).log(Level.SEVERE, null, ex);
            throw new NegocioException(ex);
        }
    }

    @Override
    public PizzaDTO obtenerPizzaPorId(int id) throws NegocioException {
        try {
            PizzaDTO pizzaDto = new PizzaDTO();
            Pizza p = pizzaDAO.obtenerPizzaPorId(id);

            pizzaDto.setIdPizza(p.getIdPizza());
            pizzaDto.setNombre(p.getNombre());
            pizzaDto.setVariante(p.getTamanio(), p.getPrecio(), p.getIdPizza());
            pizzaDto.setDescripcion(p.getDescripcion());
            pizzaDto.setRutaImagen(p.getRutaImagen());

            if (p.getEstado() == Pizza.EstadoPizza.DISPONIBLE) {
                pizzaDto.setEstado(PizzaDTO.EstadoPizza.DISPONIBLE);
            } else {
                pizzaDto.setEstado(PizzaDTO.EstadoPizza.NO_DISPONIBLE);
            }

            LOG.info(() -> "Se obtuvo la pizza: " + pizzaDto.toString());

            return pizzaDto;

        } catch (PersistenciaException ex) {
            LOG.severe(() -> "No fue posible obtener la pizza " + ex);
            throw new NegocioException(ex);
        }
    }

    @Override
    public ArrayList<PizzaDTO> agruparPizzasPorNombre() throws NegocioException {
        Map<String, PizzaDTO> pizzasFiltradas = new LinkedHashMap<>();
        List<PizzaDTO> pizzas = obtenerProductos();
        for (PizzaDTO pizza : pizzas) {
            if (!pizzasFiltradas.containsKey(pizza.getNombre())) {
                pizzasFiltradas.put(pizza.getNombre(), pizza);
            } else {
                //guardamos los ids, tamaños y precios de las demas pizzas.
                String tamanio = pizza.getTamanios().get(0);
                double precio = pizza.getPrecios().get(0);
                int id = pizza.getIdPizza();
                pizzasFiltradas.get(pizza.getNombre()).setVariante(tamanio, precio, id);
            }
        }
        return new ArrayList(pizzasFiltradas.values());
    }

    @Override
    public PizzaProductoDTO obtenerPizzaPorTamano(String nombre, String tamPizza) throws NegocioException {
        try{
            Pizza pizza = pizzaDAO.obtenerPizzaPorTamano(nombre, tamPizza);
            
            PizzaDTO.EstadoPizza estadoDto;
              if (pizza.getEstado() == Pizza.EstadoPizza.DISPONIBLE) {
                estadoDto = PizzaDTO.EstadoPizza.DISPONIBLE;
            } else {
                estadoDto = PizzaDTO.EstadoPizza.NO_DISPONIBLE;
            }
            return new PizzaProductoDTO(pizza.getIdPizza(), pizza.getNombre(), 
                                        pizza.getDescripcion(), pizza.getRutaImagen(), pizza.getTamanio(), 
                                        pizza.getPrecio(), estadoDto);
        }catch (PersistenciaException ex) {
             throw new NegocioException("No se logro obtener la pizza");
        }

    }

    @Override
    public void actualizarPrecioPizza(String nombre, String tam, double nuevoPrecio) throws NegocioException {
        try {
            pizzaDAO.actualizarPrecioPizza(nombre, tam, nuevoPrecio);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudo actualizar el precio de la pizza " + nombre + " tamaño " + tam, ex);
        }
    }

    @Override
    public void actualizarEstadoPizza(int idPizza) throws NegocioException {
        try {
           pizzaDAO.actualizarEstadoPizza(idPizza);
        } catch (PersistenciaException ex) {
            throw new NegocioException("No se pudo actualizar el estado de la pizza");
        }
    }
}
