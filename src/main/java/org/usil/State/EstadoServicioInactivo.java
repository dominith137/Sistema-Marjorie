package org.usil.State;

import org.usil.Modelo.Servicio;

public class EstadoServicioInactivo implements EstadoServicio {
    @Override
    public boolean estaActivo() {
        return false;
    }

    @Override
    public String getNombre() {
        return "Inactivo";
    }

    @Override
    public void activar(Servicio servicio) {
        servicio.setEstado(new EstadoServicioActivo());
    }

    @Override
    public void desactivar(Servicio servicio) {
        // Ya está inactivo, no hace nada
    }
}

