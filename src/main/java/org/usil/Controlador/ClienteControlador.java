package org.usil.Controlador;

import org.usil.Modelo.Cliente;
import java.time.LocalDate;
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

    // Validar y agregar cliente (retorna resultado con mensaje)
    public ResultadoOperacion validarYAgregarCliente(String nombre, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return ResultadoOperacion.error("El nombre es obligatorio");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            return ResultadoOperacion.error("El teléfono es obligatorio");
        }
        
        agregarCliente(nombre.trim(), telefono.trim());
        return ResultadoOperacion.exito("Cliente agregado exitosamente");
    }

    // Agregar cliente con fecha de registro específica (para cargar desde archivo)
    public void agregarClienteConFecha(int id, String nombre, String telefono, LocalDate fechaRegistro) {
        Cliente nuevo = new Cliente(id, nombre, telefono, fechaRegistro);
        listaClientes.add(nuevo);
        // Actualizar contadorId si es necesario
        if (id >= contadorId) {
            contadorId = id + 1;
        }
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
