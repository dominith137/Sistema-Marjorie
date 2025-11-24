package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.State.EstadoCita;
import org.usil.Modelo.Servicio;
import org.usil.Builder.CitaBuilder;
import org.usil.Builder.CitaConcreteBuilder;
import org.usil.State.EstadoCitaProgramada;
import org.usil.State.EstadoCitaCompletada;
import org.usil.State.EstadoCitaCancelada;

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

    // Validar y parsear datos de cita desde strings
    public static class DatosCita {
        public final int clienteId;
        public final int servicioId;
        public final LocalDate fecha;
        public final LocalTime hora;
        public final String observaciones;

        private DatosCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
            this.clienteId = clienteId;
            this.servicioId = servicioId;
            this.fecha = fecha;
            this.hora = hora;
            this.observaciones = observaciones;
        }
    }

    // Validar y parsear datos de formulario de cita
    public ResultadoOperacion validarYParsearDatosCita(String clienteStr, String servicioStr, String fechaStr, java.util.Date horaDate, String observaciones) {
        // Validar cliente seleccionado
        if (clienteStr == null || clienteStr.trim().isEmpty()) {
            return ResultadoOperacion.error("Seleccione un cliente");
        }

        // Validar servicio seleccionado
        if (servicioStr == null || servicioStr.trim().isEmpty()) {
            return ResultadoOperacion.error("Seleccione un servicio");
        }

        // Validar fecha
        if (fechaStr == null || fechaStr.trim().isEmpty()) {
            return ResultadoOperacion.error("Ingrese una fecha");
        }

        // Parsear IDs
        int clienteId = parsearId(clienteStr);
        if (clienteId == -1) {
            return ResultadoOperacion.error("Cliente inválido");
        }

        int servicioId = parsearId(servicioStr);
        if (servicioId == -1) {
            return ResultadoOperacion.error("Servicio inválido");
        }

        // Parsear fecha
        LocalDate fecha = parsearFecha(fechaStr);
        if (fecha == null) {
            return ResultadoOperacion.error("Formato de fecha inválido. Use YYYY-MM-DD");
        }

        // Parsear hora
        LocalTime hora = parsearHora(horaDate);
        if (hora == null) {
            return ResultadoOperacion.error("Error al leer la hora");
        }

        return ResultadoOperacion.exito();
    }

    // Obtener datos parseados (solo si la validación fue exitosa)
    public DatosCita obtenerDatosCita(String clienteStr, String servicioStr, String fechaStr, java.util.Date horaDate, String observaciones) {
        int clienteId = parsearId(clienteStr);
        int servicioId = parsearId(servicioStr);
        LocalDate fecha = parsearFecha(fechaStr);
        LocalTime hora = parsearHora(horaDate);
        return new DatosCita(clienteId, servicioId, fecha, hora, observaciones != null ? observaciones : "");
    }

    // Validar y procesar cita (agregar o editar)
    public ResultadoOperacion validarYProcesarCita(Integer idEdicion, String clienteStr, String servicioStr, String fechaStr, java.util.Date horaDate, String observaciones) {
        // Validar y parsear datos
        ResultadoOperacion validacion = validarYParsearDatosCita(clienteStr, servicioStr, fechaStr, horaDate, observaciones);
        if (!validacion.esExitoso()) {
            return validacion;
        }

        // Obtener datos parseados
        DatosCita datos = obtenerDatosCita(clienteStr, servicioStr, fechaStr, horaDate, observaciones);

        // Procesar según modo
        boolean exito;
        if (idEdicion != null) {
            exito = editarCita(idEdicion, datos.clienteId, datos.servicioId, datos.fecha, datos.hora, datos.observaciones);
            return exito ? ResultadoOperacion.exito("Cita actualizada exitosamente")
                         : ResultadoOperacion.error("No se pudo actualizar la cita. Verifique disponibilidad.");
        } else {
            exito = programarCitaConBuilder(datos.clienteId, datos.servicioId, datos.fecha, datos.hora, datos.observaciones);
            return exito ? ResultadoOperacion.exito("Cita programada exitosamente")
                         : ResultadoOperacion.error("No se pudo programar la cita. Verifique disponibilidad o datos.");
        }
    }

    // Verificar si una cita puede ser editada
    public ResultadoOperacion puedeEditarCita(int citaId) {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            return ResultadoOperacion.error("Cita no encontrada");
        }
        if (cita.getEstado() == null || !cita.getEstado().permiteModificacion()) {
            return ResultadoOperacion.error("Solo se pueden editar citas programadas");
        }
        return ResultadoOperacion.exito();
    }

    // Verificar si una cita puede cambiar de estado
    public ResultadoOperacion puedeCambiarEstado(int citaId) {
        Cita cita = buscarCitaPorId(citaId);
        if (cita == null) {
            return ResultadoOperacion.error("Cita no encontrada");
        }
        if (cita.getEstado() == null || !cita.getEstado().permiteModificacion()) {
            return ResultadoOperacion.error("Solo se puede cambiar el estado de citas programadas");
        }
        return ResultadoOperacion.exito();
    }

    // Métodos auxiliares de parsing
    private int parsearId(String item) {
        if (item != null && item.contains(" - ")) {
            try {
                return Integer.parseInt(item.split(" - ")[0]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    private LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private LocalTime parsearHora(java.util.Date horaDate) {
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(horaDate);
            return LocalTime.of(cal.get(java.util.Calendar.HOUR_OF_DAY), cal.get(java.util.Calendar.MINUTE));
        } catch (Exception e) {
            return null;
        }
    }
}
