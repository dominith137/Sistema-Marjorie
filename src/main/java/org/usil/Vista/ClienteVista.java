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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        formPanel.add(txtNombre);

        formPanel.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        formPanel.add(txtTelefono);

        JButton btnAgregar = new JButton("Agregar Cliente");
        formPanel.add(btnAgregar);

        panel.add(formPanel, BorderLayout.NORTH);

        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Teléfono"}, 0);
        tablaClientes = new JTable(modeloTabla);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        // Acción del botón
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

        add(panel);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<Cliente> clientes = controlador.obtenerClientes();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombreCompleto(), c.getTelefono()});
        }
    }

    public void mostrar() {
        setVisible(true);
    }
}
