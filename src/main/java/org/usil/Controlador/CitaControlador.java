package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.EstadoCita;
import org.usil.Modelo.Servicio;
import org.usil.Modelo.CitaBuilder;
import org.usil.Modelo.CitaConcreteBuilder;

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

    // Programa una nueva cita validando disponibilidad (constructor directo)
    public boolean programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) return false;

        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) return false;

        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), null)) return false;

        Cita nuevaCita = new Cita(contadorId++, cliente, servicio, fecha, hora, EstadoCita.PROGRAMADA, observaciones);
        listaCitas.add(nuevaCita);
        return true;
    }

    // 🚀 NUEVO: Programa una cita usando el patrón Builder
    public boolean programarCitaConBuilder(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) return false;

        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) return false;

        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), null)) return false;

        // Usar el Builder paso a paso
        CitaBuilder builder = new CitaConcreteBuilder();
        builder.setId(contadorId++);
        builder.setCliente(cliente);
        builder.setServicio(servicio);
        builder.setFecha(fecha);
        builder.setHora(hora);
        builder.setEstado(EstadoCita.PROGRAMADA);
        builder.setObservaciones(observaciones);

        Cita nuevaCita = builder.build();
        listaCitas.add(nuevaCita);
        return true;
    }

    // Valida si hay disponibilidad en un horario específico
    public boolean validarDisponibilidad(LocalDate fecha, LocalTime hora, int duracionMinutos, Integer citaIdExcluir) {
        LocalTime horaFin = hora.plusMinutes(duracionMinutos);

        for (Cita cita : listaCitas) {
            if (citaIdExcluir != null && cita.getId() == citaIdExcluir) continue;

            if (cita.getFecha().equals(fecha) && cita.getEstado() == EstadoCita.PROGRAMADA) {
                LocalTime citaHoraInicio = cita.getHora();
                int citaDuracion = cita.getServicio().getDuracionMinutos();
                LocalTime citaHoraFin = citaHoraInicio.plusMinutes(citaDuracion);

                if (hora.isBefore(citaHoraFin) && horaFin.isAfter(citaHoraInicio)) {
                    return false;
                }
            }
        }
        return true;
    }

}
