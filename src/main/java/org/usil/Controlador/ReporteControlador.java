package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.State.EstadoCita;
import org.usil.Modelo.GestorDatos;
import org.usil.Modelo.Reporte;
import org.usil.Modelo.ReporteFormato;
import org.usil.State.EstadoCitaCompletada;
import org.usil.State.EstadoCitaCancelada;

import java.io.File;
import java.io.IOException;
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

        //  usar el método correcto del controlador
        List<Cita> citas = citaControlador.obtenerCitasPorRango(fechaInicio, fechaFin);
        reporte.setCitasFiltradas(citas);

        // Estadísticas
        reporte.setTotalCitas(citas.size());
        reporte.setCitasCompletadas((int) citas.stream()
                .filter(c -> c.getEstado() instanceof EstadoCitaCompletada)
                .count());
        reporte.setCitasCanceladas((int) citas.stream()
                .filter(c -> c.getEstado() instanceof EstadoCitaCancelada)
                .count());

        double ingresos = citas.stream()
                .filter(c -> c.getEstado() instanceof EstadoCitaCompletada)
                .mapToDouble(c -> c.getServicio().getPrecio())
                .sum();
        reporte.setTotalIngresos(ingresos);

        // Servicios más solicitados
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

        List<Cita> citasCompletadas = citaControlador.obtenerCitasCompletadasPorRango(fechaInicio, fechaFin);
        reporte.setCitasFiltradas(citasCompletadas);

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
            Class<?> tipoEstado = estado.getClass();
            return citas.stream()
                    .filter(c -> c.getEstado() != null && c.getEstado().getClass().equals(tipoEstado))
                    .collect(Collectors.toList());
        }

        return citas;
    }

    // Genera un reporte completo con todas las estadísticas
    public Reporte generarReporteCompleto(LocalDate fechaInicio, LocalDate fechaFin) {
        return generarReporteCitas(fechaInicio, fechaFin);
    }

    // Contexto del patrón Strategy: decide qué formato usar
    public void exportarReporte(Reporte reporte, String tipoFormato, File archivoDestino) throws IOException {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        ReporteFormato formato = ReporteFactory.crearFormato(tipoFormato);
        formato.generar(reporte, archivoDestino);
    }

    public void exportarPDF(Reporte reporte, File archivoDestino) throws IOException {
        exportarReporte(reporte, "pdf", archivoDestino);
    }

    public void exportarConsola(Reporte reporte) throws IOException {
        exportarReporte(reporte, "consola", null);
    }
}
