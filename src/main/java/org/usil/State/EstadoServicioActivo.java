package org.usil.State;

import org.usil.Modelo.Servicio;

public class EstadoServicioActivo implements EstadoServicio {
    @Override
    public boolean estaActivo() {
        return true;
    }

    @Override
    public String getNombre() {
        return "Activo";
    }

    @Override
    public void activar(Servicio servicio) {
        // Ya está activo, no hace nada
    }

    @Override
    public void desactivar(Servicio servicio) {
        servicio.setEstado(new EstadoServicioInactivo());
    }
}

