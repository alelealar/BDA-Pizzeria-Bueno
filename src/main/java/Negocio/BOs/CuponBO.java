/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio.BOs;

import Negocio.DTOs.CuponDTO;
import Negocio.excepciones.NegocioException;
import persistencia.daos.ICuponDAO;
import persistencia.dominio.Cupon;
import persistencia.excepciones.PersistenciaException;

/**
 *
 * @author RAYMUNDO
 */
public class CuponBO implements ICuponBO {

    private final ICuponDAO cuponDAO;

    public CuponBO(ICuponDAO cuponDAO) {
        this.cuponDAO = cuponDAO;
    }

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