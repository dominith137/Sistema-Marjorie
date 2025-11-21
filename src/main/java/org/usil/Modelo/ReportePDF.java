package org.usil.Modelo;

public class ReportePDF implements ReporteFormato {
    @Override
    public void generar(Reporte reporte) {
        System.out.println("=== GENERANDO PDF ===");
        System.out.println(reporte.obtenerResumen());
        // Aquí podrías usar iText para generar el archivo real
    }
}
