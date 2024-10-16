package bo.sddpi.reactivatic.modulos.reportes.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class EmpresasRepImpl implements IEmpresasRep {

    private Map<Integer, String> formacionMap = new HashMap<>() {{
        put(1, "INICIAL");
        put(2, "PRIMARIA");
        put(3, "SECUNDARIA");
        put(4, "TECNICO");
        put(5, "LICENCIATURA");
    }};
    
    private Map<Integer, String> estadocivilMap = new HashMap<>() {{
        put(1, "SOLTERO(A)");
        put(2, "CASADO(A)");
        put(3, "VIUDO(A)");
        put(4, "CONVIVIENTE");
        put(5, "SEPARADO(A)");
    }};
    
    private Map<Integer, String> hijosMap = new HashMap<>() {{
        put(1, "1 HIJO");
        put(2, "2 HIJOS");
        put(3, "3 HIJOS");
        put(4, "4 HIJOS");
        put(5, "5 A MAS HIJOS");
        put(6, "SIN HIJOS");
    }};
    
    private Map<Integer, String> capacidadMap = new HashMap<>() {{
        put(1, "1 A 2");
        put(2, "3 A 5");
        put(3, "6 A 10");
        put(4, "11 A 15");
        put(5, "16 A 25");
        put(6, "26 A 50");
        put(7, "> 50");
    }};
    
    private Map<Integer, String> motivoMap = new HashMap<>() {{
        put(1, "POR INFLUENCIA FAMILIAR");
        put(2, "POR EXPERIENCIA LABORAL");
        put(3, "POR DESPIDO DE TRABAJO");
        put(4, "POR LA DEMANDA DEL MERCADO");
        put(5, "OTRO");
      }};
    
    private Map<Integer, String> involucradosMap = new HashMap<>() {{
        put(1, "PAREJA");
        put(2, "HIJOS");
        put(3, "HERMANOS");
        put(4, "PADRES");
        put(5, "PRIMOS");
        put(6, "OTROS");
    }};
    
    private Map<Integer, String> trabajadoresMap = new HashMap<>() {{
        put(1, "1 A 2");
        put(2, "3 A 4");
        put(3, "7 A 8");
        put(4, "9 A 10");
        put(5, "10 A 11");
        put(6, "12 A  MAS");
    }};
    
    private Map<Integer, String> feriasMap = new HashMap<>() {{
        put(1, "LOCAL");
        put(2, "NACIONAL");
        put(3, "MUNICIPAL");
        put(4, "INTERNACIONAL");
        put(5, "NINGUNO");
    }};

    Font FONT_DATO = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATON = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    Font FONT_TITULO_TABLA = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    Font FONT_DATO_TABLA = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    Font FONT_DATO_P = new Font(Font.FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);

    @Override
    public byte[] datosXLS(List<Empresas> datos, List<String> columnas) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Empresas");

        Row headerRow = sheet.createRow(0);
        
        for (int i = 0; i < columnas.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas.get(i));
            CellStyle style = workbook.createCellStyle();
            //style.setBold(true);
            cell.setCellStyle(style);
        }

        for (int i = 0; i < datos.size(); i++) {
            Row dataRow = sheet.createRow(i + 1);
            Empresas empresa = datos.get(i);
            
            for (int j = 0; j < columnas.size(); j++) {
                Cell cell = dataRow.createCell(j);
                String columna = columnas.get(j);

                switch (columna) {
                    case "nform":
                        cell.setCellValue(empresa.getNform());
                        break;
                    case "fechareg":
                        Date fechareg = empresa.getFechareg(); 
                        if (fechareg != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaFormateada = sdf.format(fechareg);
                            cell.setCellValue(fechaFormateada);
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    case "nombre":
                        cell.setCellValue(empresa.getRepresentante().getPersona().getPrimerapellido() + " " + empresa.getRepresentante().getPersona().getSegundoapellido() + " " + empresa.getRepresentante().getPersona().getPrimernombre());
                        break;
                    case "genero":
                        cell.setCellValue(empresa.getRepresentante().getPersona().getTipogenero().getTipogenero());
                        break;
                    case "celular":
                        cell.setCellValue(empresa.getCelular());
                        break;
                    case "dip":
                        cell.setCellValue(empresa.getRepresentante().getPersona().getDip());
                        break;
                    case "formacion":
                        Integer formacionKey = empresa.getRepresentante().getPersona().getFormacion();
                        String formacionValor = formacionMap.getOrDefault(formacionKey, "");
                        cell.setCellValue(formacionValor);
                        break;
                    case "estadocivil":
                        Integer estadoCivilKey = empresa.getRepresentante().getPersona().getEstadocivil();
                        String estadoCivilValor = estadocivilMap.getOrDefault(estadoCivilKey, "");
                        cell.setCellValue(estadoCivilValor);
                        break;
                    case "hijos":
                        Integer hijosKey = empresa.getRepresentante().getPersona().getHijos();
                        String hijosValor = hijosMap.getOrDefault(hijosKey, "");
                        cell.setCellValue(hijosValor);
                        break;
                    case "empresa":
                        String nombreEmpresa = empresa.getEmpresa(); 
                        String nombreRazonSocial = empresa.getRazonsocial(); 
                        cell.setCellValue((nombreEmpresa != null && !nombreEmpresa.isEmpty()) ? nombreEmpresa : nombreRazonSocial);
                        break;
                    case "nit":
                        cell.setCellValue(empresa.getNit());
                        break;
                    case "bancamovil":
                        Boolean bancamovilValor = empresa.getBancamovil(); 
                        if (bancamovilValor != null) {
                            cell.setCellValue(bancamovilValor ? "SI" : "NO"); 
                        } else {
                            cell.setCellValue("NO");
                        }
                        break;
                    case "fechaapertura":
                        Date fechaApertura = empresa.getFechaapertura(); 
                        if (fechaApertura != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String fechaFormateada = sdf.format(fechaApertura);
                            cell.setCellValue(fechaFormateada);
                        } else {
                            cell.setCellValue("");
                        }
                        break;
                    
                    case "rubro":
                        cell.setCellValue(empresa.getRubro().getRubro());
                        break;
                    case "servicios":
                        cell.setCellValue(empresa.getServicios());
                        break;
                    case "capacidad":
                        Integer capacidadKey = empresa.getCapacidad();
                        String capacidadValor = capacidadMap.getOrDefault(capacidadKey, "");
                        cell.setCellValue(capacidadValor);
                        break;
                    case "motivo":
                        Integer motivoKey = empresa.getMotivo();
                        String motivoValor = motivoMap.getOrDefault(motivoKey, "");
                        cell.setCellValue(motivoValor);
                        break;
                    case "familiar":
                        Boolean familiarValor = empresa.getFamiliar(); 
                        if (familiarValor != null) {
                            cell.setCellValue(familiarValor ? "SI" : "NO"); 
                        } else {
                            cell.setCellValue("NO"); 
                        }
                        break;
                    case "involucrados":
                        Integer involucradoKey = empresa.getInvolucrados();
                        String involucradoValor = involucradosMap.getOrDefault(involucradoKey, "");
                        cell.setCellValue(involucradoValor);
                        break;
                    case "trabajadores":
                        Integer trabajadorKey = empresa.getTrabajadores();
                        String trabajadorValor = trabajadoresMap.getOrDefault(trabajadorKey, "");
                        cell.setCellValue(trabajadorValor);
                        break;
                    case "participacion":
                        Integer feriasKey = empresa.getParticipacion();
                        String feriasValor = feriasMap.getOrDefault(feriasKey, "");
                        cell.setCellValue(feriasValor);
                        break;
                    case "capacitacion":
                        cell.setCellValue(empresa.getCapacitacion());
                        break;
                    case "municipio":
                        if (empresa.getMunicipio() != null) {
                            cell.setCellValue(empresa.getMunicipio().getMunicipio());
                        } else {
                            cell.setCellValue("");
                        }
                    break;
                    case "zona":
                        cell.setCellValue(empresa.getZona());
                        break;
                    case "direccion":
                        cell.setCellValue(empresa.getDireccion());
                        break;
                    case "referencia":
                        cell.setCellValue(empresa.getReferencia());
                        break;
                    case "transporte":
                        cell.setCellValue(empresa.getTransporte());
                        break;
                    
                    default:
                        cell.setCellValue("");
                }
            }
        }

        for (int i = 0; i < columnas.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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
