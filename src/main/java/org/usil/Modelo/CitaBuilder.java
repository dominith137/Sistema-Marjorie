package org.usil.Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

// Interfaz Builder para construir objetos Cita paso a paso
public interface CitaBuilder {
    void setId(int id);
    void setCliente(Cliente cliente);
    void setServicio(Servicio servicio);
    void setFecha(LocalDate fecha);
    void setHora(LocalTime hora);
    void setEstado(EstadoCita estado);
    void setObservaciones(String observaciones);

    // Devuelve la cita ya construida
    Cita build();
}
