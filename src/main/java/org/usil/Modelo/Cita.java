package org.usil.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

// Modelo que representa una cita en el sistema
public class Cita {
    private int id;
    private Cliente cliente;
    private Servicio servicio;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private String observaciones;

    // Constructor completo
    public Cita(int id, Cliente cliente, Servicio servicio, LocalDate fecha, LocalTime hora, EstadoCita estado, String observaciones) {
        this.id = id;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones != null ? observaciones : "";
    }

    // Constructor sin observaciones
    public Cita(int id, Cliente cliente, Servicio servicio, LocalDate fecha, LocalTime hora, EstadoCita estado) {
        this(id, cliente, servicio, fecha, hora, estado, "");
    }

    // Getters
    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    // Setters
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones != null ? observaciones : "";
    }

    // Cambia el estado a completada
    public void completar() {
        this.estado = EstadoCita.COMPLETADA;
    }

    // Cambia el estado a cancelada
    public void cancelar() {
        this.estado = EstadoCita.CANCELADA;
    }

    // Verifica si la cita está programada
    public boolean estaProgramada() {
        return this.estado == EstadoCita.PROGRAMADA;
    }

    // Obtiene el nombre completo del cliente
    public String getNombreCliente() {
        return cliente != null ? cliente.getNombreCompleto() : "N/A";
    }

    // Obtiene el nombre del servicio
    public String getNombreServicio() {
        return servicio != null ? servicio.getNombre() : "N/A";
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", cliente=" + getNombreCliente() +
                ", servicio=" + getNombreServicio() +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", estado=" + estado.getNombreEspanol() +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}

