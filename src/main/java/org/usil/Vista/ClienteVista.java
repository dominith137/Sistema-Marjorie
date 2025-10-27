package org.usil.Vista;

import org.usil.Controlador.ClienteControlador;
import org.usil.Modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClienteVista extends JFrame {
    private ClienteControlador controlador;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    public ClienteVista(ClienteControlador controlador) {
        this.controlador = controlador;
        setTitle("Gestión de Clientes");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
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

        // Acción del botón Agregar
        btnAgregar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            if (!nombre.isEmpty() && !telefono.isEmpty()) {
                controlador.agregarCliente(nombre, telefono);
                actualizarTabla();
                txtNombre.setText("");
                txtTelefono.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Completa todos los campos");
            }
        });

        // Acción del botón Eliminar
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaClientes.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                controlador.eliminarCliente(id);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar");
            }
        });

        add(panel);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Cliente> clientes = controlador.obtenerClientes();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getNombreCompleto(),
                    c.getTelefono(),
                    c.getFechaRegistro()
            });
        }
    }

    public void mostrar() {
        setVisible(true);
    }
}
