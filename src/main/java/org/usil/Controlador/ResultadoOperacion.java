package org.usil.Controlador;

// Clase para encapsular el resultado de una operación
public class ResultadoOperacion {
    private boolean exito;
    private String mensaje;

    private ResultadoOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public static ResultadoOperacion exito(String mensaje) {
        return new ResultadoOperacion(true, mensaje);
    }

    public static ResultadoOperacion exito() {
        return new ResultadoOperacion(true, null);
    }

    public static ResultadoOperacion error(String mensaje) {
        return new ResultadoOperacion(false, mensaje);
    }

    public boolean esExitoso() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}

