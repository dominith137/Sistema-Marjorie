package org.usil;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.Vista.ClienteVista;
import org.usil.Vista.ServicioVista;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Servicios
            /* ServicioControlador servicioCtrl = new ServicioControlador();
            ServicioVista servicioUI = new ServicioVista(servicioCtrl);
            servicioUI.mostrar(); */

            ClienteControlador clienteCtrl = new ClienteControlador();
            ClienteVista clienteUI = new ClienteVista(clienteCtrl);
            clienteUI.mostrar();
        });
    }
}
