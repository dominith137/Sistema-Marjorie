package org.usil.Controlador;

public class MenuPrincipalControlador {
    private ClienteControlador clienteControlador;
    private ServicioControlador servicioControlador;

    public MenuPrincipalControlador() {
        // Inicializar controladores
        this.clienteControlador = new ClienteControlador();
        this.servicioControlador = new ServicioControlador();

    }


    // Getters para acceso a los controladores desde otras partes del sistema
    public ClienteControlador getClienteControlador() {
        return clienteControlador;
    }

    public ServicioControlador getServicioControlador() {
        return servicioControlador;
    }
}
