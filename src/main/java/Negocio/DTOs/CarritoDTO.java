package dtos;

import java.time.LocalDateTime;

public class CarritoDTO {

    private int idCarrito;
    private int idUsuario;
    private LocalDateTime fechaCreacion;
    private boolean activo;

    // Constructor vacío
    public CarritoDTO() {
    }

    // Constructor sin id (para crear)
    public CarritoDTO(int idUsuario, LocalDateTime fechaCreacion, boolean activo) {
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    // Constructor completo
    public CarritoDTO(int idCarrito, int idUsuario, LocalDateTime fechaCreacion, boolean activo) {
        this.idCarrito = idCarrito;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    // Getters y Setters
    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}