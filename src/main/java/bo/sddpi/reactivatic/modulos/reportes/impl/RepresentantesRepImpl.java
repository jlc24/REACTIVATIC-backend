package bo.sddpi.reactivatic.modulos.reportes.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.reportes.IRepresentantesRep;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class RepresentantesRepImpl implements IRepresentantesRep {

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    @Override
    public byte[] datosXLS(List<Usuarios> datos) throws IOException {
        String[] columnas = { "Nro.", "Usuario", "Nombre", "Carnet de Identidad", "Dirección", "Teléfono", "Celular",
                "Correo Electrónico" };
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Workbook libro = new XSSFWorkbook();
            Sheet hoja = libro.createSheet("Usuarios del sistema");
            Row filacabecera = hoja.createRow(0);
            Cell celda = filacabecera.createCell(0);
            celda.setCellValue("Representants del sistema");
            hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            filacabecera = hoja.createRow(1);
            for (int col = 0; col < columnas.length; col++) {
                celda = filacabecera.createCell(col);
                celda.setCellValue(columnas[col]);
            }
            int filaid = 2;
            int id = 1;
            for (Usuarios _item : datos) {
                Row fila = hoja.createRow(filaid++);
                fila.createCell(0).setCellValue(id++);
                fila.createCell(1).setCellValue(_item.getUsuario());
                fila.createCell(2).setCellValue(_item.getPersona().getPrimerapellido());
                fila.createCell(3).setCellValue(_item.getPersona().getDip());
                fila.createCell(4).setCellValue(_item.getPersona().getDireccion());
                fila.createCell(5).setCellValue(_item.getPersona().getTelefono());
                fila.createCell(6).setCellValue(_item.getPersona().getCelular());
                fila.createCell(7).setCellValue(_item.getPersona().getCorreo());
            }
            libro.write(salida);
            libro.close();
        } catch (Exception e) {
            System.out.println("error=" + e);
        }
        return salida.toByteArray();
    }

    @Override
    public byte[] datosPDF(List<Usuarios> datos) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Document documento = new Document(PageSize.LETTER);
            documento.setMargins(20f, 20f, 20f, 20f);
            //EventosPDF evento = new EventosPDF();
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            //writer.setPageEvent(evento);
            documento.open();

            Image foto = Image.getInstance(new ClassPathResource("/logo.png").getURL());
            foto.setAlignment(Chunk.ALIGN_MIDDLE);
            foto.setAbsolutePosition(65, 695);
            foto.scaleAbsolute(162, 50);

            PdfPTable tabla;

            tabla = new PdfPTable(8);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(4);
            try {
                tabla.setWidths(new int[] { 1, 3, 5, 3, 3, 3, 3, 3 });
            } catch (final DocumentException e) {
                e.printStackTrace();
            }

            PdfPCell hcelda = new PdfPCell(foto);
            hcelda.setColspan(2);
            hcelda.setHorizontalAlignment(Element.ALIGN_LEFT);
            hcelda.setBorderWidthRight(0);
            hcelda.setBorderWidthBottom(0);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase(" ", FONT_DATO_TABLA));
            hcelda.setColspan(6);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("REPRESENTANTES DEL SISTEMA", FONT_DATON));
            hcelda.setColspan(8);
            hcelda.setBorderWidthTop(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Usuario", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nombre", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Carnet de Identidad", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Dirección", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Teléfono", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Celular", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Correo Electrónico", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            AtomicInteger _contador = new AtomicInteger(1);
            datos.forEach((Usuarios _item) -> {
                PdfPCell celda = new PdfPCell(new Phrase(_contador.toString(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getUsuario(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getPrimerapellido(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getDip(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getDireccion(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getTelefono(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getCelular(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getCorreo(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                _contador.incrementAndGet();
            });

            documento.add(tabla);

            documento.close();
        } catch (Exception e) {
            System.out.println("error=" + e);
        }
        return salida.toByteArray();
    }

}
