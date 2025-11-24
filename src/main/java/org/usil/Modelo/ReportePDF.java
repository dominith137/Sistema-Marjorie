package org.usil.Modelo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class ReportePDF implements ReporteFormato {

    @Override
    public void generar(Reporte reporte, File archivoDestino) throws IOException {
        if (archivoDestino == null) {
            throw new IllegalArgumentException("El archivo de destino no puede ser nulo.");
        }

        PDDocument document = new PDDocument();
        try {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            try {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(14);
                contentStream.newLineAtOffset(50, 750);

                String resumen = reporte.obtenerResumen();
                String[] lineas = resumen.split("\\r?\\n");

                for (String linea : lineas) {
                    contentStream.showText(linea);
                    contentStream.newLine();
                }

                contentStream.endText();
            } finally {
                contentStream.close();
            }

            document.save(archivoDestino);
        } finally {
            document.close();
        }
    }
}
