package org.usil.Modelo;

// Enum que representa los estados de una cita
public enum EstadoCita {
    // Cita agendada y pendiente
    PROGRAMADA,
    
    // Cita completada exitosamente
    COMPLETADA,
    
    // Cita cancelada
    CANCELADA;
    
    // Obtiene el nombre del estado en español
    public String getNombreEspanol() {
        switch (this) {
            case PROGRAMADA:
                return "Programada";
            case COMPLETADA:
                return "Completada";
            case CANCELADA:
                return "Cancelada";
            default:
                return this.name();
        }
    }
    
    // Verifica si se puede modificar la cita
    public boolean permiteModificacion() {
        return this == PROGRAMADA;
    }
    
    // Verifica si se puede cancelar la cita
    public boolean permiteCancelacion() {
        return this == PROGRAMADA;
    }
}

