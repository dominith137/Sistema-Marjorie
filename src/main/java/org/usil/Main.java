package org.usil;

import org.usil.Controlador.ClienteControlador;
import org.usil.Vista.ClienteVista;

public class Main {
    public static void main(String[] args) {
        ClienteControlador controlador = new ClienteControlador();
        ClienteVista vista = new ClienteVista(controlador);
        vista.mostrar();
    }
}
