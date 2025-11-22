package org.usil.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {
    private int id;
    private Cliente cliente;
    private Servicio servicio;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private String observaciones;

    public Cita() {
        this.id = 0;
        this.cliente = null;
        this.servicio = null;
        this.fecha = null;
        this.hora = null;
        this.estado = new EstadoCitaProgramada();
        this.observaciones = "";
    }

    public Cita(int id, Cliente cliente, Servicio servicio, LocalDate fecha, LocalTime hora, String observaciones) {
        this.id = id;
        this.cliente = cliente;
        this.servicio = servicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = new EstadoCitaProgramada();
        this.observaciones = observaciones != null ? observaciones : "";
    }

    // Constructor sin observaciones
    public Cita(int id, Cliente cliente, Servicio servicio, LocalDate fecha, LocalTime hora) {
        this(id, cliente, servicio, fecha, hora, "");
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
    public void setId(int id) {
        this.id = id;
    }

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

    public void completar() {
        if (estado != null) {
            estado.completar(this);
        }
    }

    public void cancelar() {
        if (estado != null) {
            estado.cancelar(this);
        }
    }

    public boolean estaProgramada() {
        return estado instanceof EstadoCitaProgramada;
    }

    public String getNombreCliente() {
        return cliente != null ? cliente.getNombreCompleto() : "N/A";
    }

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
                ", estado=" + (estado != null ? estado.getNombreEspanol() : "N/A") +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
