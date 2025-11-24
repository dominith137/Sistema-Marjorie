package org.usil.Strategy;

import org.usil.Modelo.Reporte;

import java.io.File;
import java.io.IOException;

public interface ReporteFormato {
    void generar(Reporte reporte, File archivoDestino) throws IOException;
}

