package org.usil.Facade;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.Controlador.CitaControlador;
import org.usil.Controlador.ResultadoOperacion;
import org.usil.Modelo.Cita;
import org.usil.Modelo.Cliente;
import org.usil.Modelo.Servicio;
import org.usil.Modelo.GestorDatos;
import org.usil.State.EstadoCita;
import org.usil.State.EstadoCitaCompletada;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Facade que proporciona una interfaz simplificada para operaciones del sistema
public class SistemaFacade {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private CitaControlador citaControlador;
    private GestorDatos gestorDatos; // Singleton

    public SistemaFacade(ClienteControlador clienteControlador, ServicioControlador servicioControlador, 
                        CitaControlador citaControlador) {
        this.clienteControlador = clienteControlador;
        this.servicioControlador = servicioControlador;
        this.citaControlador = citaControlador;
        this.gestorDatos = GestorDatos.getInstancia(); // Usar Singleton directamente
    }

    // Operaciones de Cliente
    public ResultadoOperacion registrarCliente(String nombre, String telefono) {
        ResultadoOperacion resultado = clienteControlador.validarYAgregarCliente(nombre, telefono);
        if (resultado.esExitoso()) {
            gestorDatos.guardarClientes(clienteControlador);
        }
        return resultado;
    }

    public boolean eliminarCliente(int id) {
        boolean exito = clienteControlador.eliminarCliente(id);
        if (exito) {
            gestorDatos.guardarClientes(clienteControlador);
        }
        return exito;
    }

    public List<Cliente> obtenerClientes() {
        return clienteControlador.obtenerClientes();
    }

    // Operaciones de Servicio
    public ResultadoOperacion registrarServicio(String nombre, String descripcion, double precio, int duracionMinutos) {
        boolean exito = servicioControlador.agregarServicio(nombre, descripcion, precio, duracionMinutos);
        if (exito) {
            gestorDatos.guardarServicios(servicioControlador);
            return ResultadoOperacion.exito("Servicio registrado exitosamente");
        }
        return ResultadoOperacion.error("Error al registrar el servicio");
    }

    public ResultadoOperacion actualizarServicio(int id, String nombre, String descripcion, double precio, int duracionMinutos) {
        boolean exito = servicioControlador.actualizarServicio(id, nombre, descripcion, precio, duracionMinutos);
        if (exito) {
            gestorDatos.guardarServicios(servicioControlador);
            return ResultadoOperacion.exito("Servicio actualizado exitosamente");
        }
        return ResultadoOperacion.error("Error al actualizar el servicio");
    }

    public boolean alternarEstadoServicio(int id) {
        boolean exito = servicioControlador.alternarEstadoServicio(id);
        if (exito) {
            gestorDatos.guardarServicios(servicioControlador);
        }
        return exito;
    }

    public List<Servicio> obtenerServicios() {
        return servicioControlador.obtenerTodosLosServicios();
    }

    public List<Servicio> obtenerServiciosActivos() {
        return servicioControlador.obtenerServiciosActivos();
    }

    public Servicio buscarServicioPorId(int id) {
        return servicioControlador.buscarServicioPorId(id);
    }

    public Cliente buscarClientePorId(int id) {
        return clienteControlador.buscarClientePorId(id);
    }

    // Operaciones de Cita
    public ResultadoOperacion programarCita(int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        boolean registrada = citaControlador.programarCitaConBuilder(clienteId, servicioId, fecha, hora, observaciones);
        if (registrada) {
            gestorDatos.guardarCitas(citaControlador, clienteControlador, servicioControlador);
            return ResultadoOperacion.exito("Cita programada exitosamente");
        }
        return ResultadoOperacion.error("No se pudo programar la cita. Verifique disponibilidad o datos.");
    }

    public ResultadoOperacion editarCita(int citaId, int clienteId, int servicioId, LocalDate fecha, LocalTime hora, String observaciones) {
        boolean exito = citaControlador.editarCita(citaId, clienteId, servicioId, fecha, hora, observaciones);
        if (exito) {
            gestorDatos.guardarCitas(citaControlador, clienteControlador, servicioControlador);
            return ResultadoOperacion.exito("Cita actualizada exitosamente");
        }
        return ResultadoOperacion.error("No se pudo actualizar la cita. Verifique disponibilidad.");
    }

    public ResultadoOperacion cambiarEstadoCita(int citaId, EstadoCita nuevoEstado) {
        citaControlador.cambiarEstado(citaId, nuevoEstado);
        gestorDatos.guardarCitas(citaControlador, clienteControlador, servicioControlador);
        String mensaje = nuevoEstado instanceof EstadoCitaCompletada ? "Cita completada" : "Cita cancelada";
        return ResultadoOperacion.exito(mensaje);
    }

    public List<Cita> obtenerAgendaDiaria(LocalDate fecha) {
        return citaControlador.obtenerAgendaDiaria(fecha);
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaControlador.obtenerTodasLasCitas();
    }

    public Cita buscarCitaPorId(int citaId) {
        return citaControlador.buscarCitaPorId(citaId);
    }

    // Validar y procesar cita (agregar o editar) - usa métodos del Facade
    public ResultadoOperacion validarYProcesarCita(Integer idEdicion, String clienteStr, String servicioStr, 
                                                   String fechaStr, java.util.Date horaDate, String observaciones) {
        // Validar y parsear datos usando el controlador
        ResultadoOperacion validacion = citaControlador.validarYParsearDatosCita(clienteStr, servicioStr, fechaStr, horaDate, observaciones);
        if (!validacion.esExitoso()) {
            return validacion;
        }

        // Obtener datos parseados
        CitaControlador.DatosCita datos = citaControlador.obtenerDatosCita(clienteStr, servicioStr, fechaStr, horaDate, observaciones);

        // Usar métodos del Facade según modo
        if (idEdicion != null) {
            return editarCita(idEdicion, datos.clienteId, datos.servicioId, datos.fecha, datos.hora, datos.observaciones);
        } else {
            return programarCita(datos.clienteId, datos.servicioId, datos.fecha, datos.hora, datos.observaciones);
        }
    }

    // Verificar si una cita puede ser editada
    public ResultadoOperacion puedeEditarCita(int citaId) {
        return citaControlador.puedeEditarCita(citaId);
    }

    // Verificar si una cita puede cambiar de estado
    public ResultadoOperacion puedeCambiarEstado(int citaId) {
        return citaControlador.puedeCambiarEstado(citaId);
    }

    // Guardar todos los datos
    public void guardarTodosLosDatos() {
        gestorDatos.guardarClientes(clienteControlador);
        gestorDatos.guardarServicios(servicioControlador);
        gestorDatos.guardarCitas(citaControlador, clienteControlador, servicioControlador);
    }
}

