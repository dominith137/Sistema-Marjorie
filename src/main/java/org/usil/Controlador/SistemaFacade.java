package org.usil.Controlador;

import org.usil.Modelo.Cita;
import org.usil.Modelo.EstadoCita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Facade para simplificar operaciones del sistema
public class SistemaFacade {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private CitaControlador citaControlador;

    public SistemaFacade(ClienteControlador clienteControlador, ServicioControlador servicioControlador) {
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
        this.citaControlador = new CitaControlador(clienteControlador, servicioControlador);
    }

    // Agrega un cliente
    public void registrarCliente(String nombre, String telefono) {
        clienteControlador.agregarCliente(nombre, telefono);
    }

    // Agrega un servicio
    public void registrarServicio(String nombre, double precio, int duracionMinutos) {
        servicioControlador.agregarServicio(nombre, precio, duracionMinutos);
    }

    // Programa una cita usando Builder
    public boolean programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        return citaControlador.programarCitaConBuilder(clienteId, servicioId, fecha, hora, observaciones);
    }

    // Cambia el estado de una cita
    public void cambiarEstadoCita(int citaId, EstadoCita nuevoEstado) {
        citaControlador.cambiarEstado(citaId, nuevoEstado);
    }

    // Consulta agenda diaria
    public List<Cita> obtenerAgendaDiaria(LocalDate fecha) {
        return citaControlador.obtenerAgendaDiaria(fecha);
    }

    // Consulta todas las citas
    public List<Cita> obtenerTodasLasCitas() {
        return citaControlador.obtenerTodasLasCitas();
    }

    // Acceso directo a controlador de citas (opcional)
    public CitaControlador getCitaControlador() {
        return citaControlador;
    }
}
