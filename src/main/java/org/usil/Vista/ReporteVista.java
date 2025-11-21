package org.usil.Vista;

import org.usil.Controlador.ReporteControlador;
import org.usil.Modelo.Cita;
import org.usil.Modelo.Reporte;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

// Vista para generar y visualizar reportes del sistema
public class ReporteVista extends JPanel {
    private ReporteControlador controlador;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> comboTipoReporte;
    private JTable tablaReporte;
    private DefaultTableModel modeloTabla;
    private JTextArea txtResumen;
    private JButton btnGenerar;
    private JButton btnExportar;
    private JButton btnExportarPDF;

    public ReporteVista(ReporteControlador controlador) {
        this.controlador = controlador;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel superior con controles
        JPanel panelControles = crearPanelControles();
        add(panelControles, BorderLayout.NORTH);

        // Panel central con resultados
        JPanel panelResultados = crearPanelResultados();
        add(panelResultados, BorderLayout.CENTER);
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Parámetros del Reporte"));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Fecha inicio
        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaInicio = new JTextField(12);
        txtFechaInicio.setText(LocalDate.now().minusMonths(1).toString());
        form.add(txtFechaInicio, gbc);

        // Fecha fin
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Fecha Fin (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaFin = new JTextField(12);
        txtFechaFin.setText(LocalDate.now().toString());
        form.add(txtFechaFin, gbc);

        // Tipo de reporte
        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Tipo de Reporte:"), gbc);
        gbc.gridx = 1;
        comboTipoReporte = new JComboBox<>(new String[]{
                "Reporte Completo",
                "Reporte de Citas",
                "Reporte de Ingresos",
                "Servicios Más Solicitados"
        });
        form.add(comboTipoReporte, gbc);

        // Botones
        JPanel botonesPanel = new JPanel(new FlowLayout());
        btnGenerar = new JButton("Generar Reporte");
        btnExportar = new JButton("Exportar CSV");
        btnExportarPDF = new JButton("Exportar PDF");
        btnExportar.setEnabled(false);
        botonesPanel.add(btnGenerar);
        botonesPanel.add(btnExportar);
        btnExportarPDF.setEnabled(false); // solo se activa si hay reporte
        botonesPanel.add(btnExportarPDF);

        panel.add(form, BorderLayout.CENTER);
        panel.add(botonesPanel, BorderLayout.SOUTH);

        // Acciones
        btnGenerar.addActionListener(e -> generarReporte());
        btnExportar.addActionListener(e -> exportarCSV());

        return panel;
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de resumen
        JPanel panelResumen = new JPanel(new BorderLayout());
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen"));
        txtResumen = new JTextArea(6, 50);
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panelResumen.add(new JScrollPane(txtResumen), BorderLayout.CENTER);

        // Panel de tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Detalle de Citas"));

        modeloTabla = new DefaultTableModel(new Object[]{
                "ID", "Fecha", "Hora", "Cliente", "Servicio", "Precio", "Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaReporte = new JTable(modeloTabla);
        tablaReporte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panelTabla.add(new JScrollPane(tablaReporte), BorderLayout.CENTER);

        // Dividir en dos paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelResumen, panelTabla);
        splitPane.setResizeWeight(0.3);
        splitPane.setDividerLocation(150);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void generarReporte() {
        LocalDate fechaInicio = parsearFecha(txtFechaInicio.getText());
        LocalDate fechaFin = parsearFecha(txtFechaFin.getText());

        if (fechaInicio == null || fechaFin == null) {
            JOptionPane.showMessageDialog(this, "Ingrese fechas válidas en formato YYYY-MM-DD");
            return;
        }

        if (fechaInicio.isAfter(fechaFin)) {
            JOptionPane.showMessageDialog(this, "La fecha de inicio debe ser anterior a la fecha de fin");
            return;
        }

        String tipoReporte = (String) comboTipoReporte.getSelectedItem();
        Reporte reporte = null;

        switch (tipoReporte) {
            case "Reporte Completo":
                reporte = controlador.generarReporteCompleto(fechaInicio, fechaFin);
                break;
            case "Reporte de Citas":
                reporte = controlador.generarReporteCitas(fechaInicio, fechaFin);
                break;
            case "Reporte de Ingresos":
                reporte = controlador.generarReporteIngresos(fechaInicio, fechaFin);
                break;
            case "Servicios Más Solicitados":
                mostrarServiciosMasSolicitados(fechaInicio, fechaFin);
                return;
        }

        if (reporte != null) {
            mostrarReporte(reporte);
            btnExportar.setEnabled(true);
        }
    }

    private void mostrarReporte(Reporte reporte) {
        // Mostrar resumen
        txtResumen.setText(reporte.obtenerResumen());

        // Mostrar citas en tabla
        modeloTabla.setRowCount(0);
        List<Cita> citas = reporte.getCitasFiltradas();
        for (Cita c : citas) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getFecha().toString(),
                    c.getHora().toString(),
                    c.getNombreCliente(),
                    c.getNombreServicio(),
                    String.format("S/ %.2f", c.getServicio().getPrecio()),
                    c.getEstado().getNombreEspanol()
            });
        }
    }

    private void mostrarServiciosMasSolicitados(LocalDate fechaInicio, LocalDate fechaFin) {
        Map<String, Integer> servicios = controlador.obtenerServiciosMasSolicitados(fechaInicio, fechaFin);

        // Actualizar resumen
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== SERVICIOS MÁS SOLICITADOS ===\n");
        resumen.append("Período: ").append(fechaInicio).append(" a ").append(fechaFin).append("\n\n");

        int posicion = 1;
        for (Map.Entry<String, Integer> entry : servicios.entrySet()) {
            resumen.append(posicion).append(". ").append(entry.getKey())
                    .append(": ").append(entry.getValue()).append(" veces\n");
            posicion++;
        }
        txtResumen.setText(resumen.toString());

        // Limpiar tabla
        modeloTabla.setRowCount(0);
        btnExportar.setEnabled(false);
    }

    private void exportarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        int resultado = fileChooser.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File archivo = fileChooser.getSelectedFile();
                if (!archivo.getName().endsWith(".csv")) {
                    archivo = new java.io.File(archivo.getAbsolutePath() + ".csv");
                }

                try (java.io.PrintWriter writer = new java.io.PrintWriter(archivo, "UTF-8")) {
                    // Escribir encabezados
                    for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                        if (i > 0) writer.print(";");
                        writer.print(modeloTabla.getColumnName(i));
                    }
                    writer.println();

                    // Escribir datos
                    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                        for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                            if (j > 0) writer.print(";");
                            Object valor = modeloTabla.getValueAt(i, j);
                            writer.print(valor != null ? valor.toString() : "");
                        }
                        writer.println();
                    }
                }

                JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a: " + archivo.getName());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + e.getMessage());
            }
        }
    }

    private LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public void actualizarDatos() {
        // Actualizar si hay datos cargados
    }
}