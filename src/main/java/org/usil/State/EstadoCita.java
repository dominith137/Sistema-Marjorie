package org.usil.State;

import org.usil.Modelo.Cita;

public interface EstadoCita {

    String getNombreEspanol();
    boolean permiteModificacion();
    boolean permiteCancelacion();
    void completar(Cita cita);
    void cancelar(Cita cita);
}

