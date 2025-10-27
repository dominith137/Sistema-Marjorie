package org.usil.Controlador;

import org.usil.Modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteControlador {
    private List<Cliente> listaClientes = new ArrayList<>();
    private int contadorId = 1;

    // Agregar cliente
    public void agregarCliente(String nombre, String telefono) {
        Cliente nuevo = new Cliente(contadorId++, nombre, telefono);
        listaClientes.add(nuevo);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return listaClientes;
    }

    // Buscar cliente por ID
    public Cliente buscarClientePorId(int id) {
        for (Cliente c : listaClientes) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    // Editar cliente
    public boolean editarCliente(int id, String nuevoNombre, String nuevoTelefono) {
        Cliente cliente = buscarClientePorId(id);
        if (cliente != null) {
            cliente.setNombreCompleto(nuevoNombre);
            cliente.setTelefono(nuevoTelefono);
            return true;
        }
        return false;
    }

    // Eliminar cliente
    public boolean eliminarCliente(int id) {
        return listaClientes.removeIf(c -> c.getId() == id);
    }
}
