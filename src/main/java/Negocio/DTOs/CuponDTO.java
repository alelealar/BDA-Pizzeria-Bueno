/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */


public class CuponDTO {
    private String idCupon;
    private double porcentajeDescuento;
    private LocalDateTime vigencia;
    private int cantidadUsos;

    public CuponDTO(String idCupon, double porcentajeDescuento, LocalDateTime vigencia, int cantidadUsos) {
        this.idCupon = idCupon;
        this.porcentajeDescuento = porcentajeDescuento;
        this.vigencia = vigencia;
        this.cantidadUsos = cantidadUsos;
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

    public LocalDateTime getVigencia() {
        return vigencia;
    }

    public void setVigencia(LocalDateTime vigencia) {
        this.vigencia = vigencia;
    }

    public int getCantidadUsos() {
        return cantidadUsos;
    }

    public void setCantidadUsos(int cantidadUsos) {
        this.cantidadUsos = cantidadUsos;
    }
 
    
}
