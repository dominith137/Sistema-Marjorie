package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.EstadoCita;
import org.usil.Modelo.GestorDatos;
import org.usil.Modelo.Reporte;
import org.usil.Modelo.ReporteFormato;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Controlador para generar reportes del sistema
public class ReporteControlador {
    private CitaControlador citaControlador;

    // Constructor
    public ReporteControlador(CitaControlador citaControlador) {
        this.citaControlador = citaControlador;
    }

    // Genera un reporte de citas en un rango de fechas
    public Reporte generarReporteCitas(LocalDate fechaInicio, LocalDate fechaFin) {
        Reporte reporte = new Reporte(fechaInicio, fechaFin);

        // Obtener todas las citas en el rango
        List<Cita> citas = citaControlador.obtenerCitasPorRango(fechaInicio, fechaFin);
        reporte.setCitasFiltradas(citas);

        // Calcular estadísticas
        reporte.setTotalCitas(citas.size());
        reporte.setCitasCompletadas((int) citas.stream()
                .filter(c -> c.getEstado() == EstadoCita.COMPLETADA)
                .count());
        reporte.setCitasCanceladas((int) citas.stream()
                .filter(c -> c.getEstado() == EstadoCita.CANCELADA)
                .count());

        // Calcular ingresos (solo de citas completadas)
        double ingresos = citas.stream()
                .filter(c -> c.getEstado() == EstadoCita.COMPLETADA)
                .mapToDouble(c -> c.getServicio().getPrecio())
                .sum();
        reporte.setTotalIngresos(ingresos);

        // Contar servicios más solicitados
        Map<String, Integer> serviciosContador = new HashMap<>();
        citas.forEach(cita -> {
            String nombreServicio = cita.getServicio().getNombre();
            serviciosContador.put(nombreServicio,
                    serviciosContador.getOrDefault(nombreServicio, 0) + 1);
        });
        reporte.setServiciosMasSolicitados(serviciosContador);

        return reporte;
    }

    // Genera un reporte de ingresos en un rango de fechas
    public Reporte generarReporteIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        Reporte reporte = new Reporte(fechaInicio, fechaFin);

        // Obtener solo citas completadas
        List<Cita> citasCompletadas = citaControlador.obtenerCitasCompletadasPorRango(fechaInicio, fechaFin);
        reporte.setCitasFiltradas(citasCompletadas);

        // Calcular ingresos
        double ingresos = citasCompletadas.stream()
                .mapToDouble(c -> c.getServicio().getPrecio())
                .sum();
        reporte.setTotalIngresos(ingresos);
        reporte.setCitasCompletadas(citasCompletadas.size());
        reporte.setTotalCitas(citasCompletadas.size());

        return reporte;
    }

    // Obtiene los servicios más solicitados en un rango de fechas
    public Map<String, Integer> obtenerServiciosMasSolicitados(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cita> citas = citaControlador.obtenerCitasPorRango(fechaInicio, fechaFin);

        Map<String, Integer> serviciosContador = new HashMap<>();
        citas.forEach(cita -> {
            String nombreServicio = cita.getServicio().getNombre();
            serviciosContador.put(nombreServicio,
                    serviciosContador.getOrDefault(nombreServicio, 0) + 1);
        });

        // Ordenar por cantidad (descendente) y limitar a top 10
        return serviciosContador.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        HashMap::new
                ));
    }

    // Calcula los ingresos totales en un rango de fechas
    public double calcularIngresos(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cita> citasCompletadas = citaControlador.obtenerCitasCompletadasPorRango(fechaInicio, fechaFin);
        return citasCompletadas.stream()
                .mapToDouble(c -> c.getServicio().getPrecio())
                .sum();
    }

    // Filtra citas por rango de fechas y estado
    public List<Cita> filtrarCitasPorRango(LocalDate fechaInicio, LocalDate fechaFin, EstadoCita estado) {
        List<Cita> citas = citaControlador.obtenerCitasPorRango(fechaInicio, fechaFin);

        if (estado != null) {
            return citas.stream()
                    .filter(c -> c.getEstado() == estado)
                    .collect(Collectors.toList());
        }

        return citas;
    }

    // Genera un reporte completo con todas las estadísticas
    public Reporte generarReporteCompleto(LocalDate fechaInicio, LocalDate fechaFin) {
        return generarReporteCitas(fechaInicio, fechaFin);
    }

    // ==========================
    // NUEVO: Exportar en PDF usando Factory
    // ==========================
    public void exportarPDF(Reporte reporte) {
        ReporteFormato formato = (ReporteFormato) ReporteFactory.crearReporte("pdf");
        formato.generar(reporte);
    }
}
