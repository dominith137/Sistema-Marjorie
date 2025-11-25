package org.usil.Controlador;

import org.usil.Facade.SistemaFacade;
import org.usil.Modelo.GestorDatos;

// Controlador principal que coordina todos los módulos del sistema
public class MenuPrincipalControlador {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private CitaControlador citaControlador;
    private ReporteControlador reporteControlador;
    private GestorDatos gestorDatos;
    private SistemaFacade sistemaFacade;

    public MenuPrincipalControlador() {
        // Inicializar gestor de datos (Singleton)
        this.gestorDatos = GestorDatos.getInstancia();
        
        // Inicializar controladores base
        this.clienteControlador = new ClienteControlador();
        this.servicioControlador = new ServicioControlador();
        
        // Cargar datos guardados de clientes y servicios
        gestorDatos.cargarClientes(clienteControlador);
        gestorDatos.cargarServicios(servicioControlador);
        
        // Inicializar controladores que dependen de otros
        this.citaControlador = new CitaControlador(clienteControlador, servicioControlador);
        this.reporteControlador = new ReporteControlador(citaControlador);
        
        // Cargar citas después de inicializar el controlador
        gestorDatos.cargarCitas(citaControlador, clienteControlador, servicioControlador);
        
        // Inicializar Facade con los controladores (el Facade obtiene el Singleton internamente)
        this.sistemaFacade = new SistemaFacade(clienteControlador, servicioControlador, citaControlador);
    }

    // Guarda todos los datos en archivos (usa Facade internamente)
    public void guardarDatos() {
        sistemaFacade.guardarTodosLosDatos();
    }

    // Getters para acceso a los controladores desde otras partes del sistema
    public ClienteControlador getClienteControlador() {
        return clienteControlador;
    }

    public ServicioControlador getServicioControlador() {
        return servicioControlador;
    }

    public CitaControlador getCitaControlador() {
        return citaControlador;
    }

    public ReporteControlador getReporteControlador() {
        return reporteControlador;
    }

    public GestorDatos getGestorDatos() {
        return gestorDatos;
    }

    public SistemaFacade getSistemaFacade() {
        return sistemaFacade;
    }
}
