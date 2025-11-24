package org.usil.Modelo;

public class ReporteConsola implements ReporteFormato {

    @Override
    public void generar(Reporte reporte) {
        System.out.println("=== REPORTE EN CONSOLA ===");
        System.out.println(reporte.obtenerResumen());
    }
}