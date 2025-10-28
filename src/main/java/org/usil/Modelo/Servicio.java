package org.usil.Modelo;

import java.math.BigDecimal;

public class Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private int duracionMinutos;
    private boolean activo;

    public Servicio(int id, String nombre, String descripcion, BigDecimal precio, int duracionMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.activo = true; // Por defecto, los servicios están activos
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public boolean isActivo() { return activo; }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Método para desactivar servicio (soft delete)
    public void desactivar() {
        this.activo = false;
    }

    // Método para reactivar servicio
    public void reactivar() {
        this.activo = true;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracionMinutos=" + duracionMinutos +
                ", activo=" + activo +
                '}';
    }
}
