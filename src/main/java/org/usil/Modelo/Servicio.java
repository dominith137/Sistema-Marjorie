package org.usil.Modelo;

public class Servicio {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int duracionMin;
    private boolean activo;

    public Servicio(int id, String nombre, String descripcion, double precio, int duracionMin) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracionMin = duracionMin;
        this.activo = true;
    }

    // Getters
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public double getPrecio() {
        return precio;
    }
    public int getDuracionMin() {
        return duracionMin;
    }
    public boolean isActivo() {
        return activo;
    }

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
    public void setDuracionMin(int duracionMin) {
        this.duracionMin = duracionMin;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void validar() {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser positivo");
        }
        if (duracionMin <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero");
        }
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracionMin=" + duracionMin +
                ", activo=" + activo +
                '}';
    }


}
