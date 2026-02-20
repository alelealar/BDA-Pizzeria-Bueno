/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.DTOs;

import java.time.LocalDate;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */


public class CuponDTO {
    private String idCupon;
    private double porcentajeDescuento;
    private LocalDate vigencia;

    public CuponDTO(String idCupon, double porcentajeDescuento, LocalDate vigencia) {
        this.idCupon = idCupon;
        this.porcentajeDescuento = porcentajeDescuento;
        this.vigencia = vigencia;
    }

    public CuponDTO() {
    }

    public String getIdCupon() {
        return idCupon;
    }

    public void setIdCupon(String idCupon) {
        this.idCupon = idCupon;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDate getVigencia() {
        return vigencia;
    }

    public void setVigencia(LocalDate vigencia) {
        this.vigencia = vigencia;
    }
 
}
