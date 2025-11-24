package org.usil.Modelo;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.CitaControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.State.EstadoCita;
import org.usil.State.EstadoCitaProgramada;
import org.usil.State.EstadoCitaCompletada;
import org.usil.State.EstadoCitaCancelada;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

// Gestor de persistencia de datos en archivos de texto
public class GestorDatos {
    private static final String DIRECTORIO_DATOS = "data";
    private static final String ARCHIVO_CLIENTES = "clientes.txt";
    private static final String ARCHIVO_SERVICIOS = "servicios.txt";
    private static final String ARCHIVO_CITAS = "citas.txt";
    private static final String DELIMITADOR = ";";

    // Guarda todos los clientes en archivo
    public boolean guardarClientes(ClienteControlador controlador) {
        crearDirectorioSiNoExiste();
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_CLIENTES);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Cliente cliente : controlador.obtenerClientes()) {
                String linea = cliente.getId() + DELIMITADOR +
                        cliente.getNombreCompleto() + DELIMITADOR +
                        cliente.getTelefono() + DELIMITADOR +
                        cliente.getFechaRegistro().toString();
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar clientes: " + e.getMessage());
            return false;
        }
    }

    // Carga todos los clientes desde archivo
    public boolean cargarClientes(ClienteControlador controlador) {
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_CLIENTES);
        if (!archivo.exists()) {
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(DELIMITADOR);
                if (partes.length >= 3) {
                    try {
                        int id = Integer.parseInt(partes[0].trim());
                        String nombre = partes[1].trim();
                        String telefono = partes[2].trim();
                        // Si hay fecha guardada, usarla; si no, usar fecha actual
                        LocalDate fechaRegistro;
                        if (partes.length >= 4 && !partes[3].trim().isEmpty()) {
                            fechaRegistro = LocalDate.parse(partes[3].trim());
                        } else {
                            fechaRegistro = LocalDate.now();
                        }
                        controlador.agregarClienteConFecha(id, nombre, telefono, fechaRegistro);
                    } catch (Exception e) {
                        System.err.println("Error al cargar cliente: " + linea);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
            return false;
        }
    }

    // Guarda todos los servicios en archivo
    public boolean guardarServicios(ServicioControlador controlador) {
        crearDirectorioSiNoExiste();
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_SERVICIOS);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Servicio servicio : controlador.obtenerTodosLosServicios()) {
                String linea = servicio.getId() + DELIMITADOR +
                        servicio.getNombre() + DELIMITADOR +
                        servicio.getDescripcion() + DELIMITADOR +
                        servicio.getPrecio() + DELIMITADOR +
                        servicio.getDuracionMinutos() + DELIMITADOR +
                        servicio.isActivo();
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar servicios: " + e.getMessage());
            return false;
        }
    }

    // Carga todos los servicios desde archivo
    public boolean cargarServicios(ServicioControlador controlador) {
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_SERVICIOS);
        if (!archivo.exists()) {
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(DELIMITADOR);
                if (partes.length >= 5) {
                    try {
                        String nombre = partes[1].trim();
                        String descripcion = partes.length > 2 ? partes[2].trim() : "";
                        double precio = Double.parseDouble(partes[3].trim());
                        int duracion = Integer.parseInt(partes[4].trim());
                        boolean activo = partes.length > 5 ? Boolean.parseBoolean(partes[5].trim()) : true;

                        controlador.agregarServicio(nombre, descripcion, precio, duracion);
                        if (!activo) {

                        }
                    } catch (Exception e) {
                        System.err.println("Error al cargar servicio: " + linea);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar servicios: " + e.getMessage());
            return false;
        }
    }

    // Guarda todas las citas en archivo
    public boolean guardarCitas(CitaControlador controlador,
                                ClienteControlador clienteControlador,
                                ServicioControlador servicioControlador) {
        crearDirectorioSiNoExiste();
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_CITAS);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
            for (Cita cita : controlador.obtenerTodasLasCitas()) {
                String linea = cita.getId() + DELIMITADOR +
                        cita.getCliente().getId() + DELIMITADOR +
                        cita.getServicio().getId() + DELIMITADOR +
                        cita.getFecha().toString() + DELIMITADOR +
                        cita.getHora().toString() + DELIMITADOR +
                        (cita.getEstado() != null ? cita.getEstado().getNombreEspanol() : "Programada") + DELIMITADOR +
                        (cita.getObservaciones() != null ? cita.getObservaciones().replace("\n", " ") : "");
                writer.write(linea);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar citas: " + e.getMessage());
            return false;
        }
    }

    // Carga todas las citas desde archivo
    public boolean cargarCitas(CitaControlador controlador,
                               ClienteControlador clienteControlador,
                               ServicioControlador servicioControlador) {
        File archivo = new File(DIRECTORIO_DATOS, ARCHIVO_CITAS);
        if (!archivo.exists()) {
            return true;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(DELIMITADOR);
                if (partes.length >= 6) {
                    try {
                        int clienteId = Integer.parseInt(partes[1].trim());
                        int servicioId = Integer.parseInt(partes[2].trim());
                        LocalDate fecha = LocalDate.parse(partes[3].trim());
                        LocalTime hora = LocalTime.parse(partes[4].trim());
                        EstadoCita estado = crearEstadoDesdeTexto(partes[5].trim());
                        String observaciones = partes.length > 6 ? partes[6].trim() : "";

                        Cliente cliente = clienteControlador.buscarClientePorId(clienteId);
                        Servicio servicio = servicioControlador.buscarServicioPorId(servicioId);

                        if (cliente != null && servicio != null) {
                            controlador.programarCita(clienteId, servicioId, fecha, hora, observaciones);

                            if (!(estado instanceof EstadoCitaProgramada)) {
                                Cita cita = controlador.buscarCitaPorId(controlador.obtenerTodasLasCitas().size());
                                if (cita != null) {
                                    controlador.cambiarEstado(cita.getId(), estado);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error al cargar cita: " + linea);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al cargar citas: " + e.getMessage());
            return false;
        }
    }

    // Guarda todos los datos del sistema
    public boolean guardarTodo(ClienteControlador clienteControlador,
                               ServicioControlador servicioControlador,
                               CitaControlador citaControlador) {
        boolean exito = true;
        exito &= guardarClientes(clienteControlador);
        exito &= guardarServicios(servicioControlador);
        exito &= guardarCitas(citaControlador, clienteControlador, servicioControlador);
        return exito;
    }

    // Carga todos los datos del sistema
    public boolean cargarTodo(ClienteControlador clienteControlador,
                              ServicioControlador servicioControlador,
                              CitaControlador citaControlador) {
        boolean exito = true;
        exito &= cargarClientes(clienteControlador);
        exito &= cargarServicios(servicioControlador);
        exito &= cargarCitas(citaControlador, clienteControlador, servicioControlador);
        return exito;
    }

    // Crea el directorio de datos si no existe
    private void crearDirectorioSiNoExiste() {
        File directorio = new File(DIRECTORIO_DATOS);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    // Traduce el texto del archivo al estado concreto (State)
    private EstadoCita crearEstadoDesdeTexto(String texto) {
        if (texto == null) return new EstadoCitaProgramada();

        String valor = texto.trim().toUpperCase();
        switch (valor) {
            case "COMPLETADA":
                return new EstadoCitaCompletada();
            case "CANCELADA":
                return new EstadoCitaCancelada();
            case "PROGRAMADA":
            default:
                return new EstadoCitaProgramada();
        }
    }
}
