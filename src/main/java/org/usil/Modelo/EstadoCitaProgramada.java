package org.usil.Modelo;

public class EstadoCitaProgramada implements EstadoCita{
    @Override
    public String getNombreEspanol() {
        return "Pogramada";
    }

    @Override
    public boolean permiteModificacion() {
        return true;
    }

    @Override
    public boolean permiteCancelacion() {
        return true;
    }

    @Override
    public void completar(Cita cita) {
        cita.setEstado(new EstadoCitaProgramada());
    }

    @Override
    public void cancelar(Cita cita) {
        cita.setEstado(new EstadoCitaCancelada());
    }
}
