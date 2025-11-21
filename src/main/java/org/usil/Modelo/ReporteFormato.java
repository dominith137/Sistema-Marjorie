package org.usil.Modelo;

/**
 * Interfaz para formatos de salida de reportes (PDF, Excel, etc.)
 */
public interface ReporteFormato {
    void generar(Reporte reporte);
}
