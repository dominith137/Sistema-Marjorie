package org.usil.State;

import org.usil.Modelo.Servicio;

public interface EstadoServicio {
    boolean estaActivo();
    String getNombre();
    void activar(Servicio servicio);
    void desactivar(Servicio servicio);
}

