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

import bo.sddpi.reactivatic.modulos.entidades.Solicitudes;
import bo.sddpi.reactivatic.modulos.entidades.Solicitudesproductos;
import bo.sddpi.reactivatic.modulos.reportes.ISolicitudesRep;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SolicitudesRepImpl implements ISolicitudesRep {

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    @Override
    public byte[] datosXLS(List<Solicitudes> datos) throws IOException {
        String[] columnas = { "Nro.", "Fecha", "Cliente", "Empresa", "Estado" };
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Workbook libro = new XSSFWorkbook();
            Sheet hoja = libro.createSheet("Clientes registrados con solicitudes");
            Row filacabecera = hoja.createRow(0);
            Cell celda = filacabecera.createCell(0);
            celda.setCellValue("Clientes registrados con solicitudes");
            hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            filacabecera = hoja.createRow(1);
            for (int col = 0; col < columnas.length; col++) {
                celda = filacabecera.createCell(col);
                celda.setCellValue(columnas[col]);
            }
            int filaid = 2;
            int id = 1;
            for (Solicitudes _item : datos) {
                Row fila = hoja.createRow(filaid++);
                fila.createCell(0).setCellValue(id++);
                fila.createCell(1).setCellValue(_item.getFecha().toString());
                //fila.createCell(2).setCellValue(_item.getCliente().getPersona().getPrimerapellido());
                fila.createCell(3).setCellValue(_item.getEmpresa().getEmpresa());
                if (_item.getEstado()) {
                    fila.createCell(4).setCellValue("Finalizado");
                } else {
                    fila.createCell(4).setCellValue("En proceso");
                }
                fila = hoja.createRow(filaid++);
                fila.createCell(1).setCellValue("Producto");
                fila.createCell(2).setCellValue("Cantidad");
                fila.createCell(3).setCellValue("Precio de venta");
                fila.createCell(4).setCellValue("Total");
                for (Solicitudesproductos _itemp : _item.getSolicitudproductos()) {
                    fila = hoja.createRow(filaid++);
                    fila.createCell(1).setCellValue(_itemp.getProducto().getProducto());
                    fila.createCell(2).setCellValue(_itemp.getCantidad().toString());
                    fila.createCell(3).setCellValue(_itemp.getPrecioventa().toString());
                    fila.createCell(4).setCellValue(_itemp.getTotal().toString());
                }

            }
            libro.write(salida);
            libro.close();
        } catch (Exception e) {
            System.out.println("error=" + e);
        }
        return salida.toByteArray();
    }

    @Override
    public byte[] datosPDF(List<Solicitudes> datos) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Document documento = new Document(PageSize.LETTER);
            documento.setMargins(20f, 20f, 20f, 20f);
            EventosPDF evento = new EventosPDF();
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setPageEvent(evento);
            documento.open();

            Image foto = Image.getInstance(new ClassPathResource("/logo.png").getURL());
            foto.setAlignment(Chunk.ALIGN_MIDDLE);
            foto.setAbsolutePosition(65, 695);
            foto.scaleAbsolute(162, 50);

            PdfPTable tabla;

            tabla = new PdfPTable(5);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(4);
            try {
                tabla.setWidths(new int[] { 1, 2, 4, 4, 4 });
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
            hcelda.setColspan(3);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Clientes registrados con solicitudes", FONT_DATON));
            hcelda.setColspan(5);
            hcelda.setBorderWidthTop(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Fecha", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Cliente", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Empresa", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Estado", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            AtomicInteger _contador = new AtomicInteger(1);
            datos.forEach((Solicitudes _item) -> {
                PdfPCell celda = new PdfPCell(new Phrase(_contador.toString(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getFecha().toString(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                // celda = new PdfPCell(new Phrase(_item.getCliente().getPersona().getPrimerapellido(), FONT_DATO_TABLA));
                // celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                // celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                // tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getEmpresa().getEmpresa(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                if (_item.getEstado()) {
                    celda = new PdfPCell(new Phrase("Finalizado", FONT_DATO_TABLA));
                } else {
                    celda = new PdfPCell(new Phrase("En proceso", FONT_DATO_TABLA));
                }
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                PdfPCell hcelda1 = new PdfPCell(new Phrase("Producto", FONT_TITULO_TABLA));
                hcelda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcelda1.setColspan(2);
                hcelda1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tabla.addCell(hcelda1);

                hcelda1 = new PdfPCell(new Phrase("Cantidad", FONT_TITULO_TABLA));
                hcelda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcelda1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(hcelda1);

                hcelda1 = new PdfPCell(new Phrase("Precio de venta", FONT_TITULO_TABLA));
                hcelda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcelda1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(hcelda1);

                hcelda1 = new PdfPCell(new Phrase("Total", FONT_TITULO_TABLA));
                hcelda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                hcelda1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(hcelda1);

                _item.getSolicitudproductos().forEach((Solicitudesproductos _itemp) -> {
                    PdfPCell celda1 = new PdfPCell(new Phrase(_itemp.getProducto().getProducto(), FONT_DATO_TABLA));
                    celda1.setColspan(2);
                    celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(celda1);

                    celda1 = new PdfPCell(new Phrase(_itemp.getCantidad().toString(), FONT_DATO_TABLA));
                    celda1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    celda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(celda1);

                    celda1 = new PdfPCell(new Phrase(_itemp.getPrecioventa().toString(), FONT_DATO_TABLA));
                    celda1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(celda1);

                    celda1 = new PdfPCell(new Phrase(_itemp.getTotal().toString(), FONT_DATO_TABLA));
                    celda1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    celda1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tabla.addCell(celda1);
                });

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
