package org.usil;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.ServicioControlador;
import org.usil.Vista.ClienteVista;
import org.usil.Vista.ServicioVista;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicioControlador c = new ServicioControlador();
            ServicioVista v = new ServicioVista(c);
            v.mostrar();
        });
    }
}
