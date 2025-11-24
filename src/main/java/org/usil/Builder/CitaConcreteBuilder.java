package org.usil.Builder;

import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.Servicio;
import org.usil.State.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;

public class CitaConcreteBuilder implements CitaBuilder {
    private Cita cita;


    public CitaConcreteBuilder() {
        this.cita = new  Cita();
    }

    @Override
    public void setId(int id) {
        cita.setId(id);
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

