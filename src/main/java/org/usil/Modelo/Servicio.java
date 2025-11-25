package org.usil.Modelo;

import org.usil.State.EstadoServicio;
import org.usil.State.EstadoServicioActivo;

public class Servicio {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int duracionMinutos;
    private EstadoServicio estado;

    public Servicio(int id, String nombre, String descripcion, double precio, int duracionMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMinutos = duracionMinutos;
        this.estado = new EstadoServicioActivo(); // Por defecto, los servicios están activos
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public int getDuracionMinutos() { return duracionMinutos; }
    public EstadoServicio getEstado() { return estado; }
    public boolean isActivo() { return estado.estaActivo(); }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setEstado(EstadoServicio estado) {
        this.estado = estado;
    }

    // Método para desactivar servicio
    public void desactivar() {
        estado.desactivar(this);
    }

    // Método para reactivar servicio
    public void reactivar() {
        estado.activar(this);
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracionMinutos=" + duracionMinutos +
                ", estado=" + estado.getNombre() +
                '}';
    }
}
