package org.usil.Facade;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.Controlador.CitaControlador;
import org.usil.Modelo.Cita;
import org.usil.State.EstadoCita;
import org.usil.Singleton.GestorDatos;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SistemaFacade {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private CitaControlador citaControlador;
    private GestorDatos gestorDatos; // Singleton

    public SistemaFacade(ClienteControlador clienteControlador, ServicioControlador servicioControlador) {
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
        this.citaControlador = new CitaControlador(clienteControlador, servicioControlador);
        this.gestorDatos = GestorDatos.getInstancia(); // 🔗 aquí se linkea
    }

    public void registrarCliente(String nombre, String telefono) {
        clienteControlador.agregarCliente(nombre, telefono);
        gestorDatos.guardar(nombre); // usa Singleton
    }

    public void registrarServicio(String nombre, double precio, int duracionMinutos) {
        servicioControlador.agregarServicio(nombre, precio, duracionMinutos);
        gestorDatos.guardar(nombre); // usa Singleton
    }

    public boolean programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        boolean registrada = citaControlador.programarCitaConBuilder(clienteId, servicioId, fecha, hora, observaciones);
        if (registrada) {
            gestorDatos.guardar("Cita registrada en " + fecha + " " + hora); // usa Singleton
        }
        return registrada;
    }

    public void cambiarEstadoCita(int citaId, EstadoCita nuevoEstado) {
        citaControlador.cambiarEstado(citaId, nuevoEstado);
        gestorDatos.guardar("Estado cambiado de cita " + citaId); // usa Singleton
    }

    public List<Cita> obtenerAgendaDiaria(LocalDate fecha) {
        return citaControlador.obtenerAgendaDiaria(fecha);
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaControlador.obtenerTodasLasCitas();
    }
}

