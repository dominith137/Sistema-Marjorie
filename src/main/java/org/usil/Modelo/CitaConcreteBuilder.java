package org.usil.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

// Implementación concreta del Builder para construir una Cita
public class CitaConcreteBuilder implements CitaBuilder {
    private Cita cita;

    // Inicializa una cita vacía con valores por defecto
    public CitaConcreteBuilder() {
        this.cita = new Cita(0, null, null, null, null, EstadoCita.PROGRAMADA, "");
    }

    @Override
    public void setId(int id) {
        cita.setCliente(cita.getCliente()); // mantiene cliente actual
        cita = new Cita(id, cita.getCliente(), cita.getServicio(),
                cita.getFecha(), cita.getHora(),
                cita.getEstado(), cita.getObservaciones());
    }

    @Override
    public void setCliente(Cliente cliente) {
        cita.setCliente(cliente);
    }

    @Override
    public void setServicio(Servicio servicio) {
        cita.setServicio(servicio);
    }

    @Override
    public void setFecha(LocalDate fecha) {
        cita.setFecha(fecha);
    }

    @Override
    public void setHora(LocalTime hora) {
        cita.setHora(hora);
    }

    @Override
    public void setEstado(EstadoCita estado) {
        cita.setEstado(estado);
    }

    @Override
    public void setObservaciones(String observaciones) {
        cita.setObservaciones(observaciones);
    }

    @Override
    public Cita build() {
        return cita;
    }
}
