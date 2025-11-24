package org.usil.Modelo;

import java.io.File;
import java.io.IOException;

public class ReporteConsola implements ReporteFormato {

    @Override
    public void generar(Reporte reporte, File archivoDestino) throws IOException {
        System.out.println("=== REPORTE EN CONSOLA ===");
        System.out.println(reporte.obtenerResumen());
    }
}
