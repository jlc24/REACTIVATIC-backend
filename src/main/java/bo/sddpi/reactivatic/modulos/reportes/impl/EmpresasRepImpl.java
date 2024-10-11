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

import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.reportes.IEmpresasRep;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class EmpresasRepImpl implements IEmpresasRep {

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    @Override
    public byte[] datosXLS(List<Empresas> datos) throws IOException {
        String[] columnas = { "Nro.", "Tipo", "Nombre", "Descripción", "Representante", "Carnet de Identidad", "Rubro", "Subrubro","Municipio", "Localidad", "Dirección", "Teléfono", "Celular", "Correo Electrónico" };
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Workbook libro = new XSSFWorkbook();
            Sheet hoja = libro.createSheet("Empresas");
            Row filacabecera = hoja.createRow(0);
            Cell celda = filacabecera.createCell(0);
            celda.setCellValue("Reporte de Empresas");
            hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
            filacabecera = hoja.createRow(1);
            for (int col = 0; col < columnas.length; col++) {
                celda = filacabecera.createCell(col);
                celda.setCellValue(columnas[col]);
            }
            int filaid = 2;
            int id = 1;
            for (Empresas _item : datos) {
                Row fila = hoja.createRow(filaid++);
                fila.createCell(0).setCellValue(id++);
                fila.createCell(1).setCellValue(_item.getTipo());
                fila.createCell(2).setCellValue(_item.getEmpresa());
                fila.createCell(3).setCellValue(_item.getDescripcion());
                fila.createCell(4).setCellValue(_item.getRepresentante().getPersona().getPrimerapellido());
                fila.createCell(5).setCellValue(_item.getRepresentante().getPersona().getDip());
                fila.createCell(6).setCellValue(_item.getSubrubro().getRubro().getRubro());
                fila.createCell(7).setCellValue(_item.getSubrubro().getSubrubro());
                fila.createCell(8).setCellValue(_item.getLocalidad().getMunicipio().getMunicipio());
                fila.createCell(9).setCellValue(_item.getLocalidad().getLocalidad());
                fila.createCell(10).setCellValue(_item.getDireccion());
                fila.createCell(11).setCellValue(_item.getTelefono());
                fila.createCell(12).setCellValue(_item.getCelular());
                fila.createCell(13).setCellValue(_item.getCorreo());
            }
            libro.write(salida);
            libro.close();
        } catch (Exception e) {
            System.out.println("error=" + e);
        }
        return salida.toByteArray();
    }

    @Override
    public byte[] datosPDF(List<Empresas> datos) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            Document documento = new Document(PageSize.LETTER.rotate());
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

            tabla = new PdfPTable(14);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(4);
            try {
                tabla.setWidths(new int[] { 1, 3, 5, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
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
            hcelda.setColspan(12);
            hcelda.setBorderWidthLeft(0);
            hcelda.setBorderWidthBottom(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Reporte de Empresas", FONT_DATON));
            hcelda.setColspan(14);
            hcelda.setBorderWidthTop(0);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Tipo", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Nombre", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Descripción", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Representante", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Carnet de Identidad", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Rubro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Subrubro", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Municipio", FONT_TITULO_TABLA));
            hcelda.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcelda.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(hcelda);

            hcelda = new PdfPCell(new Phrase("Localidad", FONT_TITULO_TABLA));
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
            datos.forEach((Empresas _item) -> {
                PdfPCell celda = new PdfPCell(new Phrase(_contador.toString(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getTipo(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getEmpresa(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getDescripcion(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getRepresentante().getPersona().getPrimerapellido(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getRepresentante().getPersona().getDip(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getSubrubro().getRubro().getRubro(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getSubrubro().getSubrubro(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getLocalidad().getMunicipio().getMunicipio(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getLocalidad().getLocalidad(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getDireccion(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getTelefono(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getCelular(), FONT_DATO_TABLA));
                celda.setHorizontalAlignment(Element.ALIGN_LEFT);
                celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tabla.addCell(celda);

                celda = new PdfPCell(new Phrase(_item.getCorreo(), FONT_DATO_TABLA));
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
