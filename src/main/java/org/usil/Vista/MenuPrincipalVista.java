package org.usil.Vista;

import org.usil.Controlador.MenuPrincipalControlador;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalVista extends JFrame {
    private MenuPrincipalControlador controlador;
    private JTabbedPane tabbedPane;

    public MenuPrincipalVista(MenuPrincipalControlador controlador) {
        this.controlador = controlador;
        setTitle("Sistema de Gestión - Salón de Belleza Marjorie Villegas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        configurarCierre();
    }

    // Configura el guardado automático al cerrar la ventana
    private void configurarCierre() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int opcion = JOptionPane.showConfirmDialog(
                    MenuPrincipalVista.this,
                    "¿Desea guardar los datos antes de salir?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_CANCEL_OPTION
                );
                
                if (opcion == JOptionPane.YES_OPTION) {
                    controlador.guardarDatos();
                    JOptionPane.showMessageDialog(
                        MenuPrincipalVista.this,
                        "Datos guardados exitosamente"
                    );
                    System.exit(0);
                } else if (opcion == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
                // Si es CANCEL, no hace nada y mantiene la ventana abierta
            }
        });
    }

    private void initComponents() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(248, 249, 250));

        // Header con información del salón
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central con pestañas
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        // Pestaña de Clientes - Vista integrada
        ClienteVista clientesPanel = new ClienteVista(controlador.getClienteControlador());
        tabbedPane.addTab("Clientes", clientesPanel);

        // Pestaña de Servicios - Vista integrada
        ServicioVistaPanel serviciosPanel = new ServicioVistaPanel(controlador.getServicioControlador());
        tabbedPane.addTab("Servicios", serviciosPanel);

        // Pestaña de Citas - Vista integrada
        CitaVista citasPanel = new CitaVista(
            controlador.getCitaControlador(),
            controlador.getClienteControlador(),
            controlador.getServicioControlador()
        );
        tabbedPane.addTab("Citas", citasPanel);

        // Pestaña de Reportes - Vista integrada
        ReporteVista reportesPanel = new ReporteVista(controlador.getReporteControlador());
        tabbedPane.addTab("Reportes", reportesPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 58, 64));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Salón de Belleza Marjorie Villegas");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JLabel subtitleLabel = new JLabel("Sistema de Gestión Integral");
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }


    public void mostrar() {
        setVisible(true);
    }
}
