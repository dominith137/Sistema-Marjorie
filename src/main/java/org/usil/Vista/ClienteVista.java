package org.usil.Vista;

import org.usil.Controlador.ClienteControlador;
import org.usil.Controlador.MenuPrincipalControlador;
import org.usil.Controlador.ResultadoOperacion;
import org.usil.Modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClienteVista extends JPanel {
    private MenuPrincipalControlador menuControlador;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public ClienteVista(ClienteControlador controlador, MenuPrincipalControlador menuControlador) {
        this.menuControlador = menuControlador;
        initComponents();
        actualizarTabla();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        formPanel.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        formPanel.add(txtTelefono);

        JButton btnAgregar = new JButton("Agregar Cliente");
        formPanel.add(btnAgregar);

        JButton btnEliminar = new JButton("Eliminar Cliente");
        formPanel.add(btnEliminar);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono", "Fecha"}, 0);
        tablaClientes = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        // Acción del botón Agregar (usa Facade)
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            
            ResultadoOperacion resultado = menuControlador.getSistemaFacade().registrarCliente(nombre, telefono);
            
            if (resultado.esExitoso()) {
                actualizarTabla();
                txtNombre.setText("");
                txtTelefono.setText("");
                if (resultado.getMensaje() != null) {
                    JOptionPane.showMessageDialog(this, resultado.getMensaje());
                }
            } else {
                JOptionPane.showMessageDialog(this, resultado.getMensaje());
            }
        });

        // Acción del botón Eliminar (usa Facade)
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaClientes.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                boolean exito = menuControlador.getSistemaFacade().eliminarCliente(id);
                if (exito) {
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el cliente");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar");
            }
        });

        add(panel);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Cliente> clientes = menuControlador.getSistemaFacade().obtenerClientes();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getNombreCompleto(),
                    c.getTelefono(),
                    c.getFechaRegistro()
            });
        }
    }

    public void actualizarDatos() {
        actualizarTabla();
    }
}
