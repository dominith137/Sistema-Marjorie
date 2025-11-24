package org.usil.Controlador;

import org.usil.Strategy.ReporteFormato;
import org.usil.Strategy.ReportePDF;
import org.usil.Strategy.ReporteConsola;

public class ReporteFactory {

    public static ReporteFormato crearFormato(String tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de formato no puede ser nulo");
        }

        if (tipo.equalsIgnoreCase("pdf")) {
            return new ReportePDF();
        } else if (tipo.equalsIgnoreCase("consola")) {
            return new ReporteConsola();
        }

        throw new IllegalArgumentException("Tipo de formato no soportado: " + tipo);
    }
}
