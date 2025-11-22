package org.usil.Modelo;

public class EstadoCitaCompletada implements EstadoCita {

    @Override
    public String getNombreEspanol() {
        return "Completada";
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
