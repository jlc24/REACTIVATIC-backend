package bo.sddpi.reactivatic.modulos.reportes.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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

import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;
import bo.sddpi.reactivatic.modulos.reportes.IPlanillasRep;

@Service
public class PlanillasRepImpl implements IPlanillasRep {

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    public byte[] planillaRegistroPDF(List<Beneficiosempresas> datos, Beneficios dato) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            Document documento = new Document(PageSize.LETTER.rotate());
            documento.setMargins(20f, 20f, 20f, 20f);

            String nombreUsuario = dato.getUsuario().getPersona().getPrimerapellido() + " " + dato.getUsuario().getPersona().getPrimernombre();

            EventosPDF evento = new EventosPDF(nombreUsuario);
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setPageEvent(evento);
            documento.open();

            // Cargar las imágenes
            Image bicentenario = cargarImagen("/bicentenario.png", 65, 695, 91, 60, Chunk.ALIGN_MIDDLE);
            Image bolivia = cargarImagen("/bolivia_horizontal.png", 65, 695, 146, 60, Chunk.ALIGN_MIDDLE);
            Image gador = cargarImagen("/logogador.png", 65, 695, 58, 60, Chunk.ALIGN_MIDDLE);
            Image reactivatic = cargarImagen("/logo.png", 65, 695, 135, 60, Chunk.ALIGN_MIDDLE);
            Image imageok = cargarImagen("/controlar.png", 65, 695, 135, 60, Chunk.ALIGN_MIDDLE);

            // Crear la tabla principal
            PdfPTable tabla = new PdfPTable(7);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(7);
            tabla.setWidths(new int[]{1, 8, 3, 5, 3, 3, 3});

            // Encabezado de las imágenes
            agregarCeldaImagen(tabla, bicentenario, 2, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, bolivia, 2, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, gador, 1, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, reactivatic, 2, Element.ALIGN_RIGHT);

            // Espacio en blanco
            agregarCeldaTextoT(tabla, " ", FONT_DATO_TABLA, 7);

            // Encabezado del documento
            agregarCeldaTextoT(tabla, "PLANILLA DE REGISTRO", FONT_DATON, 7);
            agregarCeldaTextoT(tabla, dato.getBeneficio(), FONT_DATON, 7);
            agregarCeldaTextoT(tabla, "PROYECTO APOYO A LA COMPETITIVIDAD DE LA MICRO Y PEQUEÑA EMPRESA (REACTIVA TIC) EN MUNICIPIOS PRODUCTORES DEL DEPARTAMENTO DE ORURO", FONT_DATO_TABLA, 7);
            agregarCeldaTextoT(tabla, " ", FONT_DATO_TABLA, 7);

            // Encabezados de la tabla de datos
            agregarEncabezado(tabla, "NRO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "APELLIDOS Y NOMBRES", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "CARNET", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "MUNICIPIO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "RUBRO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "CELULAR", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "FIRMA", BaseColor.LIGHT_GRAY, 20f);

            // Contador para el número de filas
            AtomicInteger contador = new AtomicInteger(1);
            float alto = 33f;

            // Rellenar la tabla con los datos
            datos.forEach(item -> {
                if (item.getEmpresa() != null && item.getEmpresa().getRepresentante() != null &&
                    item.getEmpresa().getRepresentante().getPersona() != null) {

                    agregarCeldaTexto(tabla, String.valueOf(contador.getAndIncrement()), FONT_DATO_TABLA, alto, Element.ALIGN_CENTER);
                    
                    StringBuilder nombreCompleto = new StringBuilder()
                        .append(item.getEmpresa().getRepresentante().getPersona().getPrimerapellido()).append(' ')
                        .append(item.getEmpresa().getRepresentante().getPersona().getSegundoapellido()).append(' ')
                        .append(item.getEmpresa().getRepresentante().getPersona().getPrimernombre());
                    
                    agregarCeldaTexto(tabla, nombreCompleto.toString(), FONT_DATO_TABLA, alto, Element.ALIGN_LEFT);
                    agregarCeldaTexto(tabla, item.getEmpresa().getRepresentante().getPersona().getDip(), FONT_DATO_TABLA, alto, Element.ALIGN_CENTER);
                    agregarCeldaTexto(tabla, item.getEmpresa().getMunicipio() != null ? item.getEmpresa().getMunicipio().getMunicipio() : "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER);
                    agregarCeldaTexto(tabla, item.getEmpresa().getRubro() != null ? item.getEmpresa().getRubro().getRubro() : "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER);
                    agregarCeldaTexto(tabla, item.getEmpresa().getRepresentante().getPersona().getCelular(), FONT_DATO_TABLA, alto, Element.ALIGN_CENTER);
                    agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_LEFT);
                }
            });

            documento.add(tabla);
            documento.close();

        } catch (Exception e) {
            // Puedes agregar un mejor manejo de excepciones aquí
            e.printStackTrace();
        }
        return salida.toByteArray();
    }

    // Métodos auxiliares

    private Image cargarImagen(String ruta, float posX, float posY, float ancho, float alto, int alineacion) throws IOException, DocumentException {
        Image imagen = Image.getInstance(new ClassPathResource(ruta).getURL());
        imagen.setAlignment(alineacion);  // Se establece la alineación según el parámetro
        imagen.setAbsolutePosition(posX, posY);
        imagen.scaleAbsolute(ancho, alto);
        return imagen;
    }

    private void agregarCeldaImagen(PdfPTable tabla, Image imagen, int colspan, int align) {
        PdfPCell celda = new PdfPCell(imagen);
        celda.setHorizontalAlignment(align);
        celda.setColspan(colspan);
        celda.setBorderWidth(0);
        tabla.addCell(celda);
    }

    private void agregarCeldaTextoT(PdfPTable tabla, String texto, Font fuente, int colspan) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setColspan(colspan);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(0);
        tabla.addCell(celda);
    }

    private void agregarCeldaTexto(PdfPTable tabla, String texto, Font fuente, float alto, int align) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setFixedHeight(alto);
        celda.setHorizontalAlignment(align);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBorderWidth(1);
        tabla.addCell(celda);
    }

    private void agregarEncabezado(PdfPTable tabla, String texto, BaseColor color, float alto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, FONT_TITULO_TABLA));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setBackgroundColor(color);
        celda.setFixedHeight(alto);
        tabla.addCell(celda);
    }

    @Override
    public byte[] planillaInscripcionPDF(Beneficios dato) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            Document documento = new Document(PageSize.LETTER.rotate());
            documento.setMargins(20f, 20f, 20f, 20f);

            String nombreUsuario = dato.getUsuario().getPersona().getPrimerapellido() + " " + dato.getUsuario().getPersona().getPrimernombre();

            EventosPDF evento = new EventosPDF(nombreUsuario);
            PdfWriter writer = PdfWriter.getInstance(documento, salida);
            writer.setPageEvent(evento);
            documento.open();

            // Cargar las imágenes
            Image bicentenario = cargarImagen("/bicentenario.png", 65, 695, 91, 60, Chunk.ALIGN_MIDDLE);
            Image bolivia = cargarImagen("/bolivia_horizontal.png", 65, 695, 146, 60, Chunk.ALIGN_MIDDLE);
            Image gador = cargarImagen("/logogador.png", 65, 695, 58, 60, Chunk.ALIGN_MIDDLE);
            Image reactivatic = cargarImagen("/logo.png", 65, 695, 135, 60, Chunk.ALIGN_MIDDLE);

            // Crear la tabla principal
            PdfPTable tabla = new PdfPTable(7);
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(7);
            tabla.setWidths(new int[]{1, 8, 3, 5, 3, 3, 3});

            // Encabezado de las imágenes
            agregarCeldaImagen(tabla, bicentenario, 2, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, bolivia, 2, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, gador, 1, Element.ALIGN_LEFT);
            agregarCeldaImagen(tabla, reactivatic, 2, Element.ALIGN_RIGHT);

            // Espacio en blanco
            agregarCeldaTextoT(tabla, " ", FONT_DATO_TABLA, 7);

            // Encabezado del documento
            agregarCeldaTextoT(tabla, "PLANILLA DE INSCRIPCION", FONT_DATON, 7);
            agregarCeldaTextoT(tabla, dato.getBeneficio(), FONT_DATON, 7);
            agregarCeldaTextoT(tabla, "PROYECTO APOYO A LA COMPETITIVIDAD DE LA MICRO Y PEQUEÑA EMPRESA (REACTIVA TIC) EN MUNICIPIOS PRODUCTORES DEL DEPARTAMENTO DE ORURO", FONT_DATO_TABLA, 7);
            agregarCeldaTextoT(tabla, " ", FONT_DATO_TABLA, 7);

            // Encabezados de la tabla de datos
            agregarEncabezado(tabla, "NRO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "APELLIDOS Y NOMBRES", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "CARNET", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "MUNICIPIO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "RUBRO", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "CELULAR", BaseColor.LIGHT_GRAY, 20f);
            agregarEncabezado(tabla, "FIRMA", BaseColor.LIGHT_GRAY, 20f);

            // Contador para el número de filas
            //AtomicInteger contador = new AtomicInteger(1);
            float alto = 33f;

            // Rellenar la tabla con los datos
            int filasEnBlanco = 12;

            for (int i = 0; i < filasEnBlanco; i++) {
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER); // Columna del contador vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_LEFT);   // Columna de nombre vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER); // Columna de DIP vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER); // Columna de municipio vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER); // Columna de rubro vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_CENTER); // Columna de celular vacía
                agregarCeldaTexto(tabla, "", FONT_DATO_TABLA, alto, Element.ALIGN_LEFT);   // Columna adicional vacía
            }

            documento.add(tabla);
            documento.close();

        } catch (Exception e) {
            // Puedes agregar un mejor manejo de excepciones aquí
            e.printStackTrace();
        }
        return salida.toByteArray();
    };

    @Override
    public byte[] planillaAsistenciaPDF(List<Beneficiosempresas> datos, Beneficios dato) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'planillaAsistenciaPDF'");
    };
    
    @Override
    public byte[] planillaInscripcionXLS(List<Beneficiosempresas> datos) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'planillaInscripcionXLS'");
    }

    @Override
    public byte[] planillaRegistroXLS(List<Beneficiosempresas> datos) throws IOException {
        String[] columnas = { "NRO", "APELLIDOS Y NOMBRES", "CARNET", "MUNICIPIO", "RUBRO", "CELULAR", "FIRMA" };

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
        Sheet sheet = workbook.createSheet("Planilla de Beneficios");

        // Crear el encabezado de las columnas
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            // Aplicar estilo opcional (negrita, alineación, etc.)
            CellStyle headerStyle = workbook.createCellStyle();
            //Font font = workbook.createFont();
            //font.setBold(true);
            //headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }

        // Crear las filas con los datos de la lista de 'Beneficiosempresas'
        int rowNum = 1;
        for (Beneficiosempresas beneficio : datos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum - 1); // NRO
            row.createCell(1).setCellValue(beneficio.getEmpresa().getRepresentante().getPersona().getPrimerapellido() + " " + beneficio.getEmpresa().getRepresentante().getPersona().getSegundoapellido() + " " + beneficio.getEmpresa().getRepresentante().getPersona().getPrimernombre()); // APELLIDOS Y NOMBRES
            row.createCell(2).setCellValue(beneficio.getEmpresa().getRepresentante().getPersona().getDip() != null ? beneficio.getEmpresa().getRepresentante().getPersona().getDip() : ""); // CARNET
            row.createCell(3).setCellValue(beneficio.getEmpresa().getMunicipio().getMunicipio() != null ? beneficio.getEmpresa().getMunicipio().getMunicipio() : ""); // MUNICIPIO
            row.createCell(4).setCellValue(beneficio.getEmpresa().getRubro().getRubro() != null ? beneficio.getEmpresa().getRubro().getRubro() : ""); // RUBRO
            row.createCell(5).setCellValue(beneficio.getEmpresa().getCelular() != null ? beneficio.getEmpresa().getCelular() : ""); // CELULAR
            row.createCell(6).setCellValue(""); // FIRMA vacía
        }

        // Auto-ajustar el ancho de las columnas
        for (int i = 0; i < columnas.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Escribir el archivo Excel en el ByteArrayOutputStream
        workbook.write(bos);
        return bos.toByteArray();
    }
    }

    @Override
    public byte[] planillaAsistenciaXLS(List<Beneficiosempresas> datos) throws IOException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'planillaAsistenciaXLS'");
    }

}
