package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.EstadoCita;
import org.usil.Modelo.Servicio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Controlador para gestionar las citas del sistema
public class CitaControlador {
    private List<Cita> listaCitas = new ArrayList<>();
    private int contadorId = 1;
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;

    // Constructor con referencias a otros controladores
    public CitaControlador(ClienteControlador clienteControlador, ServicioControlador servicioControlador) {
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
    }

    // Programa una nueva cita validando disponibilidad
    public boolean programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        // Validar que el cliente existe
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) {
            return false;
        }

        // Validar que el servicio existe y está activo
        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) {
            return false;
        }

        // Validar disponibilidad del horario
        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), null)) {
            return false;
        }

        // Crear y agregar la cita
        Cita nuevaCita = new Cita(contadorId++, cliente, servicio, fecha, hora, EstadoCita.PROGRAMADA, observaciones);
        listaCitas.add(nuevaCita);
        return true;
    }

    // Valida si hay disponibilidad en un horario específico
    public boolean validarDisponibilidad(LocalDate fecha, LocalTime hora, int duracionMinutos, Integer citaIdExcluir) {
        // Calcular hora de fin del servicio
        LocalTime horaFin = hora.plusMinutes(duracionMinutos);

        // Buscar citas programadas que se solapen con el horario
        for (Cita cita : listaCitas) {
            // Excluir la cita que se está editando
            if (citaIdExcluir != null && cita.getId() == citaIdExcluir) {
                continue;
            }

            // Solo considerar citas programadas en la misma fecha
            if (cita.getFecha().equals(fecha) && cita.getEstado() == EstadoCita.PROGRAMADA) {
                LocalTime citaHoraInicio = cita.getHora();
                int citaDuracion = cita.getServicio().getDuracionMinutos();
                LocalTime citaHoraFin = citaHoraInicio.plusMinutes(citaDuracion);

                // Verificar solapamiento de horarios
                if (hora.isBefore(citaHoraFin) && horaFin.isAfter(citaHoraInicio)) {
                    return false; // Hay conflicto de horario
                }
            }
        }
        return true; // No hay conflictos
    }

    // Obtiene todas las citas
    public List<Cita> obtenerTodasLasCitas() {
        return new ArrayList<>(listaCitas);
    }

    // Obtiene la agenda diaria (citas de una fecha específica)
    public List<Cita> obtenerAgendaDiaria(LocalDate fecha) {
        return listaCitas.stream()
                .filter(cita -> cita.getFecha().equals(fecha))
                .sorted((c1, c2) -> c1.getHora().compareTo(c2.getHora()))
                .collect(Collectors.toList());
    }

    // Busca una cita por ID
    public Cita buscarCitaPorId(int id) {
        return listaCitas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Cambia el estado de una cita
    public boolean cambiarEstado(int citaId, EstadoCita nuevoEstado) {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            return false;
        }

        // Validar transición de estado
        if (nuevoEstado == EstadoCita.COMPLETADA && cita.getEstado() == EstadoCita.PROGRAMADA) {
            cita.completar();
            return true;
        } else if (nuevoEstado == EstadoCita.CANCELADA && cita.getEstado() == EstadoCita.PROGRAMADA) {
            cita.cancelar();
            return true;
        }

        return false;
    }

    // Obtiene citas por rango de fechas
    public List<Cita> obtenerCitasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return listaCitas.stream()
                .filter(cita -> !cita.getFecha().isBefore(fechaInicio) && !cita.getFecha().isAfter(fechaFin))
                .sorted((c1, c2) -> {
                    int comparacionFecha = c1.getFecha().compareTo(c2.getFecha());
                    if (comparacionFecha != 0) {
                        return comparacionFecha;
                    }
                    return c1.getHora().compareTo(c2.getHora());
                })
                .collect(Collectors.toList());
    }

    // Obtiene citas completadas en un rango de fechas
    public List<Cita> obtenerCitasCompletadasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return obtenerCitasPorRango(fechaInicio, fechaFin).stream()
                .filter(cita -> cita.getEstado() == EstadoCita.COMPLETADA)
                .collect(Collectors.toList());
    }

    // Edita una cita existente
    public boolean editarCita(int citaId, int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null || !cita.getEstado().permiteModificacion()) {
            return false;
        }

        // Validar cliente
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) {
            return false;
        }

        // Validar servicio
        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) {
            return false;
        }

        // Validar disponibilidad (excluyendo la cita actual)
        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), citaId)) {
            return false;
        }

        // Actualizar la cita
        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setObservaciones(observaciones);
        return true;
    }

    // Elimina una cita (solo si está cancelada o completada)
    public boolean eliminarCita(int citaId) {
        Cita cita = buscarCitaPorId(citaId);
        if (cita != null && cita.getEstado() != EstadoCita.PROGRAMADA) {
            return listaCitas.remove(cita);
        }
        return false;
    }
}

