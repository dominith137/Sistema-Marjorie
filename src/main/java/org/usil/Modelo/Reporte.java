package org.usil.Modelo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Modelo que representa un reporte de gestión del sistema
public class Reporte {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int totalCitas;
    private int citasCompletadas;
    private int citasCanceladas;
    private double totalIngresos;
    private Map<String, Integer> serviciosMasSolicitados;
    private List<Cita> citasFiltradas;

    // Constructor
    public Reporte(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.serviciosMasSolicitados = new HashMap<>();
        this.totalIngresos = 0.0;
        this.totalCitas = 0;
        this.citasCompletadas = 0;
        this.citasCanceladas = 0;
    }

    public Reporte() {
    }

    // Getters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int getTotalCitas() {
        return totalCitas;
    }

    public int getCitasCompletadas() {
        return citasCompletadas;
    }

    public int getCitasCanceladas() {
        return citasCanceladas;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public Map<String, Integer> getServiciosMasSolicitados() {
        return serviciosMasSolicitados;
    }

    public List<Cita> getCitasFiltradas() {
        return citasFiltradas;
    }

    // Setters
    public void setTotalCitas(int totalCitas) {
        this.totalCitas = totalCitas;
    }

    public void setCitasCompletadas(int citasCompletadas) {
        this.citasCompletadas = citasCompletadas;
    }

    public void setCitasCanceladas(int citasCanceladas) {
        this.citasCanceladas = citasCanceladas;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public void setServiciosMasSolicitados(Map<String, Integer> serviciosMasSolicitados) {
        this.serviciosMasSolicitados = serviciosMasSolicitados;
    }

    public void setCitasFiltradas(List<Cita> citasFiltradas) {
        this.citasFiltradas = citasFiltradas;
    }

    // Agrega un servicio al contador
    public void agregarServicio(String nombreServicio) {
        serviciosMasSolicitados.put(nombreServicio, 
            serviciosMasSolicitados.getOrDefault(nombreServicio, 0) + 1);
    }

    // Obtiene el resumen del reporte como texto
    public String obtenerResumen() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== REPORTE DE GESTIÓN ===\n");
        resumen.append("Período: ").append(fechaInicio).append(" a ").append(fechaFin).append("\n\n");
        resumen.append("Total de Citas: ").append(totalCitas).append("\n");
        resumen.append("Citas Completadas: ").append(citasCompletadas).append("\n");
        resumen.append("Citas Canceladas: ").append(citasCanceladas).append("\n");
        resumen.append("Total de Ingresos: S/ ").append(String.format("%.2f", totalIngresos)).append("\n\n");
        resumen.append("Servicios Más Solicitados:\n");
        serviciosMasSolicitados.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(5)
            .forEach(entry -> resumen.append("  - ").append(entry.getKey())
                .append(": ").append(entry.getValue()).append(" veces\n"));
        return resumen.toString();
    }
}

