/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Negocio.DTOs;

/**
 *
 * @author Brian Kaleb Sandoval Rodríguez - 00000262741
 * @author Alejandra Leal Armenta - 00000262719
 * @author Paulina Michel Guevara Cervantez - 00000262724
 */


public class ClienteDTO {
    private int idCliente;
    private String nombres;
    private String apellidos;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String calle;
    private String numero;
    private String colonia;
    private String CP;
    private int dia;
    private int mesNum;
    private int anio;

    public ClienteDTO(int idCliente, String nombres, String apellidoPaterno, String apellidoMaterno, String calle, String numero, String colonia, String CP, int dia, int mesNum, int anio) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.CP = CP;
        this.dia = dia;
        this.mesNum = mesNum;
        this.anio = anio;
    }

    public ClienteDTO(String nombres, String apellidoPaterno, String apellidoMaterno, String calle, String numero, String colonia, String CP, int dia, int mesNum, int anio) {
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.CP = CP;
        this.dia = dia;
        this.mesNum = mesNum;
        this.anio = anio;
    }

    public ClienteDTO(String nombres, String apellidos, String calle, String numero, String colonia, String CP, int dia, int mesNum, int anio) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.CP = CP;
        this.dia = dia;
        this.mesNum = mesNum;
        this.anio = anio;
    }
    
    

    public ClienteDTO() {
    }
    

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCP() {
        return CP;
    }

    public void setCP(String CP) {
        this.CP = CP;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMesNum() {
        return mesNum;
    }

    public void setMesNum(int mesNum) {
        this.mesNum = mesNum;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }
    
    
    
}
