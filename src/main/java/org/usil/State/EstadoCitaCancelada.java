package org.usil.State;

import org.usil.Modelo.Cita;

public class EstadoCitaCancelada implements EstadoCita {

    @Override
    public String getNombreEspanol() {
        return "Cancelada";
    }

    @Override
    public boolean permiteModificacion() {
        return false;
    }

    @Override
    public boolean permiteCancelacion() {
        return false;
    }

    @Override
    public void completar(Cita cita) {
    }

    @Override
    public void cancelar(Cita cita) {
    }
}

