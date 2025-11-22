package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.EstadoCita;
import org.usil.Modelo.Servicio;
import org.usil.Modelo.CitaBuilder;
import org.usil.Modelo.CitaConcreteBuilder;
import org.usil.Modelo.EstadoCitaProgramada;
import org.usil.Modelo.EstadoCitaCompletada;
import org.usil.Modelo.EstadoCitaCancelada;

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

    public CitaControlador(ClienteControlador clienteControlador, ServicioControlador servicioControlador) {
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
    }

    public boolean programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) return false;

        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) return false;

        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), null)) return false;

        Cita nuevaCita = new Cita(contadorId++, cliente, servicio, fecha, hora, observaciones);
        listaCitas.add(nuevaCita);
        return true;
    }

    public boolean programarCitaConBuilder(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) return false;

        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) return false;

        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), null)) return false;

        CitaBuilder builder = new CitaConcreteBuilder();
        builder.setId(contadorId++);
        builder.setCliente(cliente);
        builder.setServicio(servicio);
        builder.setFecha(fecha);
        builder.setHora(hora);
        builder.setEstado(new EstadoCitaProgramada());
        builder.setObservaciones(observaciones);

        Cita nuevaCita = builder.build();
        listaCitas.add(nuevaCita);
        return true;
    }

    public boolean validarDisponibilidad(LocalDate fecha, LocalTime hora, int duracionMinutos, Integer citaIdExcluir) {
        LocalTime horaFin = hora.plusMinutes(duracionMinutos);

        for (Cita cita : listaCitas) {
            if (citaIdExcluir != null && cita.getId() == citaIdExcluir) continue;

            if (cita.getFecha().equals(fecha) && cita.getEstado() != null && cita.getEstado().permiteModificacion()) {
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

    public boolean editarCita(Integer citaEditandoId, int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        Cita cita = buscarCitaPorId(citaEditandoId);
        if (cita == null || cita.getEstado() == null || !cita.getEstado().permiteModificacion()) return false;

        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
        if (cliente == null) return false;

        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);
        if (servicio == null || !servicio.isActivo()) return false;

        if (!validarDisponibilidad(fecha, hora, servicio.getDuracionMinutos(), citaEditandoId)) return false;

        cita.setCliente(cliente);
        cita.setServicio(servicio);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setObservaciones(observaciones);
        return true;
    }

    public Cita buscarCitaPorId(int citaId) {
        return listaCitas.stream()
                .filter(c -> c.getId() == citaId)
                .findFirst()
                .orElse(null);
    }

    public void cambiarEstado(int citaId, EstadoCita nuevoEstado) {
        Cita cita = buscarCitaPorId(citaId);

        if (cita != null && cita.getEstado() != null && cita.getEstado().permiteModificacion() && nuevoEstado != null) {
            if (nuevoEstado instanceof EstadoCitaCompletada) {
                cita.completar();
            } else if (nuevoEstado instanceof EstadoCitaCancelada) {
                cita.cancelar();
            }
        }
    }

    public List<Cita> obtenerTodasLasCitas() {
        return new ArrayList<>(listaCitas);
    }

    public List<Cita> obtenerAgendaDiaria(LocalDate fecha) {
        return listaCitas.stream()
                .filter(cita -> cita.getFecha().equals(fecha))
                .sorted((c1, c2) -> c1.getHora().compareTo(c2.getHora()))
                .collect(Collectors.toList());
    }

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

    public List<Cita> obtenerCitasCompletadasPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        return obtenerCitasPorRango(fechaInicio, fechaFin).stream()
                .filter(cita -> cita.getEstado() instanceof EstadoCitaCompletada)
                .collect(Collectors.toList());
    }
}
