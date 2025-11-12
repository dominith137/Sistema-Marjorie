package org.usil.Controlador;

// Controlador principal que coordina todos los módulos del sistema
public class MenuPrincipalControlador {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;
    private CitaControlador citaControlador;
    private ReporteControlador reporteControlador;

    public MenuPrincipalControlador() {
        // Inicializar controladores base
        this.clienteControlador = new ClienteControlador();
        this.servicioControlador = new ServicioControlador();
        
        // Inicializar controladores que dependen de otros
        this.citaControlador = new CitaControlador(clienteControlador, servicioControlador);
        this.reporteControlador = new ReporteControlador(citaControlador);
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
}
