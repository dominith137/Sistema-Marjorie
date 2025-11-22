package org.usil.Modelo;

public interface EstadoCita {

    String getNombreEspanol();
    boolean permiteModificacion();
    boolean permiteCancelacion();
    void completar(Cita cita);
    void cancelar(Cita cita);
}

