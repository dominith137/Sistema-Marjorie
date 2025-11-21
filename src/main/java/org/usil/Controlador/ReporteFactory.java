package org.usil.Controlador;

import org.usil.Modelo.Reporte;
import org.usil.Modelo.ReportePDF;

public class ReporteFactory {
    public static Reporte crearReporte(String tipo) {
        if (tipo.equalsIgnoreCase("pdf")) {
            return new ReportePDF();
        }
        throw new IllegalArgumentException("Tipo de reporte no soportado: " + tipo);
    }
}
