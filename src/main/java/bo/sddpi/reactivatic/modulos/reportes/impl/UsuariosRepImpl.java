package bo.sddpi.reactivatic.modulos.reportes.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.reportes.IUsuariosRep;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class UsuariosRepImpl implements IUsuariosRep {

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    @Override
    public byte[] datosXLS(List<Usuarios> datos) throws IOException {
        String[] columnas = { "Nro.", "Nombre", "Carnet de Identidad", "Dirección", "Teléfono", "Celular",
                "Correo Electrónico", "Usuario", "Estado" };
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Workbook libro = new XSSFWorkbook();
            Sheet hoja = libro.createSheet("Usuarios del sistema");
            Row filacabecera = hoja.createRow(0);
            Cell celda = filacabecera.createCell(0);
            celda.setCellValue("Usuarios del sistema");
            hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

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
                fila.createCell(1).setCellValue(_item.getPersona().getPrimerapellido() + ' ' + _item.getPersona().getSegundoapellido() + ' ' + _item.getPersona().getPrimernombre());
                fila.createCell(2).setCellValue(_item.getPersona().getDip());
                fila.createCell(3).setCellValue(_item.getPersona().getDireccion());
                fila.createCell(4).setCellValue(_item.getPersona().getTelefono());
                fila.createCell(5).setCellValue(_item.getPersona().getCelular());
                fila.createCell(6).setCellValue(_item.getPersona().getCorreo());
                fila.createCell(7).setCellValue(_item.getRol().getNombrerol());
                String estadoTexto = _item.getEstado() ? "HABILITADO" : "INHABILITADO";
                fila.createCell(8).setCellValue(estadoTexto);
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
            Document documento = new Document(PageSize.LETTER.rotate());
            documento.setMargins(10f, 10f, 10f, 10f);
            EventosPDF evento = new EventosPDF();
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setPageEvent(evento);
            documento.open();

            Image logoRectiva = Image.getInstance(new ClassPathResource("/logo.png").getURL());
            logoRectiva.setAlignment(Chunk.ALIGN_MIDDLE);
            logoRectiva.setAbsolutePosition(65, 695);
            logoRectiva.scaleAbsolute(150, 50);

            Image logoGADOR = Image.getInstance(new ClassPathResource("/logogadorblack.png").getURL());

            Rectangle pageSize = documento.getPageSize();
            float x = (pageSize.getWidth() - logoGADOR.getScaledWidth()) / 2;
            float y = (pageSize.getHeight() - logoGADOR.getScaledHeight()) / 2;

            PdfContentByte canvas = writer.getDirectContentUnder();
            PdfGState gState = new PdfGState();
            gState.setFillOpacity(0.1f);
            canvas.setGState(gState);

            logoGADOR.setAbsolutePosition(x, y);
            canvas.addImage(logoGADOR);

            Image logoSDDPI = Image.getInstance(new ClassPathResource("/sddpi.png").getURL());
            logoSDDPI.setAlignment(Chunk.ALIGN_MIDDLE);
            logoSDDPI.setAbsolutePosition(65, 695);
            logoSDDPI.scaleAbsolute(50, 50);

            Image logoDPEIC = Image.getInstance(new ClassPathResource("/dpeic.png").getURL());
            logoDPEIC.setAlignment(Chunk.ALIGN_MIDDLE);
            logoDPEIC.setAbsolutePosition(65, 695);
            logoDPEIC.scaleAbsolute(50, 50);

            PdfPTable tabla;

            tabla = new PdfPTable(8);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(4);
            try {
                tabla.setWidths(new int[] { 1, 5, 2, 5, 2, 2, 4, 3 });
            } catch (final DocumentException e) {
                e.printStackTrace();
            }

            PdfPCell hcelda = new PdfPCell(logoDPEIC);
            hcelda.setColspan(3);
            hcelda.setHorizontalAlignment(Element.ALIGN_LEFT);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(logoSDDPI);
            hcelda.setColspan(2);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(logoRectiva);
            hcelda.setColspan(3);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase(" ", FONT_DATON));
            hcelda.setColspan(8);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("USUARIOS DEL SISTEMA", FONT_DATON));
            hcelda.setColspan(8);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);
            
            hcelda = new PdfPCell(new Phrase(" ", FONT_DATON));
            hcelda.setColspan(8);
            hcelda.setBorderWidthTop(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthRight(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nombre", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("C.I.", FONT_TITULO_TABLA));
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

            hcelda = new PdfPCell(new Phrase("Cargo", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            AtomicInteger _contador = new AtomicInteger(1);
            datos.forEach((Usuarios _item) -> {
                PdfPCell celda = new PdfPCell(new Phrase(_contador.toString(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getPersona().getPrimerapellido() + ' ' + _item.getPersona().getSegundoapellido() + ' ' + _item.getPersona().getPrimernombre(), FONT_DATO_TABLA));
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

                celda = new PdfPCell(new Phrase(_item.getRol().getNombrerol(), FONT_DATO_TABLA));
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
