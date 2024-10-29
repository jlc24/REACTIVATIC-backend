package bo.sddpi.reactivatic.modulos.ctrls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;

import bo.sddpi.reactivatic.modulos.aods.IEmpresasAod;
import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.reportes.IEmpresasRep;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;



@RestController
@RequestMapping("/apirest/empresas")
public class EmpresasCtrl {

    @Autowired
    IEmpresasAod iEmpresasAod;

    @Autowired
    IEmpresasRep iEmpresasRep;

    @Autowired
    ISubirarchivosServ iSubirarchivosServ;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "rubro", defaultValue = "") String rubro,
                            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Empresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            datos = iEmpresasAod.datos(buscar, rubro, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Empresas>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                @RequestParam(value = "rubro", defaultValue = "") String rubro) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iEmpresasAod.cantidad(buscar, rubro);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/l")
    ResponseEntity<?> lista(){
        List<Empresas> empresas = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            empresas = iEmpresasAod.lista();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Empresas>>(empresas, HttpStatus.OK);
    }
    

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Empresas dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iEmpresasAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Empresas>(dato, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Empresas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idusuario = Long.parseLong(auth.getName());
            dato.setIdusuario(idusuario);
            iEmpresasAod.adicionar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Empresas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iEmpresasAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEmpresasAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@RequestBody Empresas empresa){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEmpresasAod.cambiarestado(empresa);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificaco el estado del rubro");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/verificar/{id}")
    ResponseEntity<?> verificar(@PathVariable Long id ){
        Integer total = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            total = iEmpresasAod.total(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(total, HttpStatus.OK);
    }

    @GetMapping(value = "/perfilempresa")
    ResponseEntity<?> perfilempresa() {
        Empresas dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            dato = iEmpresasAod.perfilempresa(Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "No existe en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Empresas>(dato, HttpStatus.OK);
    }

    @PostMapping(value="/upload")
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("id") Long id,
            @RequestParam("tipo") String tipo,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> mensajes = new HashMap<>();

        if (archivo.isEmpty()) {
            mensajes.put("mensaje", "No se ha seleccionado ningún archivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            iSubirarchivosServ.uploadimagen(id, archivo, tipo);
        } catch (NumberFormatException e) {
            mensajes.put("mensaje", "El ID de usuario no es válido.");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException  e) {
            mensajes.put("mensaje", "Error al procesar el archivo: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo cargar el archivo: " + archivo.getOriginalFilename() + ". Error: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        mensajes.put("mensaje", "Se cargó el archivo con éxito: " + archivo.getOriginalFilename());
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/downloadimage")
    public ResponseEntity<?> downloadImage(@RequestParam("id") Long id, @RequestParam("tipo") String tipo){
        Map<String, Object> response = new HashMap<>();
        try {

            List<Map<String, String>> imagenes = iSubirarchivosServ.downloadimagen(id, tipo);

            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            response.put("error", "Error al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/cargare/{id}")
    ResponseEntity<?> cargar(@RequestParam("archivo") MultipartFile archivo, @PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iSubirarchivosServ.cargarimagenempresa(id, archivo);
            iSubirarchivosServ.redimensionarempresa(id);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo cargar el archivo: " + archivo.getOriginalFilename() + "!!! error:" + e.toString());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se cargo el archivo con éxito: " + archivo.getOriginalFilename());
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping(value="/descargarempresa/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargarempresa(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        Resource archivo = null;
        try {
            archivo = iSubirarchivosServ.descargarimagenempresa(id.toString());
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo descargar el archivo");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @GetMapping("/formulario/{id}")
    public ResponseEntity<?> generarFormularioPdf(@PathVariable Long id) {
        try {
            // Llamamos al servicio que genera el PDF
            byte[] pdfBytes = iSubirarchivosServ.generarFormularioPdf(id);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=formulario_" + id + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (IOException e) {
            Map<String, Object> mensajes = new HashMap<>();
            mensajes.put("mensaje", "Error: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND); // Cambiar a NOT_FOUND ya que el recurso no existe.

        } catch (DocumentException e) {
            Map<String, Object> mensajes = new HashMap<>();
            mensajes.put("mensaje", "Error al generar el PDF: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/datosXLS", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> datosXLS(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Empresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iEmpresasAod.datosrepo(buscar);
            //data = iEmpresasRep.datosXLS(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/datosPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<?> datosPDF(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Empresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iEmpresasAod.datosrepo(buscar);
            data = iEmpresasRep.datosPDF(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/reportePDF")//, produces = MediaType.APPLICATION_PDF_VALUE
    ResponseEntity<?> reportesPDF(@RequestParam(value = "columnsemp", required = false) List<String> columnsemp,
                                  @RequestParam(value = "columnsrub", required = false) List<String> columnsrub,
                                  @RequestParam(value = "columnsmun", required = false) List<String> columnsmun,
                                  @RequestParam(value = "columnsrep", required = false) List<String> columnsrep,
                                  @RequestParam(value = "columnsper", required = false) List<String> columnsper,
                                  @RequestParam(value = "municipio", defaultValue = "allM") String municipio,
                                  @RequestParam(value = "rubro", defaultValue = "allR") String rubro,
                                  @RequestParam(value = "fecharegistro", defaultValue = "allReg") String fecharegistro,
                                  @RequestParam(value = "orden", defaultValue = "fechareg") String orden,
                                  @RequestParam(value = "direccion", defaultValue = "ASC") String direccion){
    
        byte[] data = null;
        Map<String, Object> parametros = new HashMap<>();
        List<String> columns = new ArrayList<>();
        
        parametros.put("columnsemp", columnsemp);
        parametros.put("columnsrub", columnsrub);
        parametros.put("columnsmun", columnsmun);
        parametros.put("columnsrep", columnsrep);
        parametros.put("columnsper", columnsper);
        parametros.put("municipio", municipio);
        parametros.put("rubro", rubro);
        parametros.put("fecharegistro", fecharegistro);
        parametros.put("ordenCampo", orden);
        parametros.put("ordenDireccion", direccion);

        if (columnsemp != null) {
            columnsemp.forEach(col -> columns.add(col));
        }
        if (columnsrub != null) {
            columnsrub.forEach(col -> columns.add(col));
        }
        if (columnsmun != null) {
            columnsmun.forEach(col -> columns.add(col));
        }
        if (columnsrep != null) {
            columnsrep.forEach(col -> columns.add(col));
        }
        if (columnsper != null) {
            columnsper.forEach(col -> columns.add(col));
        }
        
        List<Empresas> empresas = iEmpresasAod.obtenerEmpresasDinamico(parametros);
        
    
        return ResponseEntity.ok(empresas);
    }
    
    @GetMapping(value = "/reporteXLS", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> reportesXLS(@RequestParam(value = "columnsemp", required = false) List<String> columnsemp,
                                  @RequestParam(value = "columnsrub", required = false) List<String> columnsrub,
                                  @RequestParam(value = "columnsmun", required = false) List<String> columnsmun,
                                  @RequestParam(value = "columnsrep", required = false) List<String> columnsrep,
                                  @RequestParam(value = "columnsper", required = false) List<String> columnsper,
                                  @RequestParam(value = "municipio", defaultValue = "allM") String municipio,
                                  @RequestParam(value = "rubro", defaultValue = "allR") String rubro,
                                  @RequestParam(value = "fecharegistro", defaultValue = "allReg") String fecharegistro,
                                  @RequestParam(value = "orden", defaultValue = "fechareg") String orden,
                                  @RequestParam(value = "direccion", defaultValue = "ASC") String direccion) throws IOException{
    
        byte[] data = null;
        Map<String, Object> parametros = new HashMap<>();
        List<String> columns = new ArrayList<>();
        Map<String, Object> mensajes = new HashMap<>();

        parametros.put("columnsrep", columnsrep);
        parametros.put("columnsper", columnsper);
        parametros.put("columnsemp", columnsemp);
        parametros.put("columnsrub", columnsrub);
        parametros.put("columnsmun", columnsmun);
        parametros.put("municipio", municipio);
        parametros.put("rubro", rubro);
        parametros.put("fecharegistro", fecharegistro);
        parametros.put("ordenCampo", orden);
        parametros.put("ordenDireccion", direccion); 

        BiConsumer<List<String>, List<String>> addFilteredColumns = (sourceColumns, targetColumns) -> {
            if (sourceColumns != null) {
                boolean hasNombreFields = sourceColumns.contains("primerapellido") &&
                                  sourceColumns.contains("segundoapellido") &&
                                  sourceColumns.contains("primernombre");
                if (hasNombreFields) {
                    targetColumns.add("nombre");
                }

                sourceColumns.stream()
                    .filter(col -> !col.equalsIgnoreCase("idrepresentante") && 
                                   !col.equalsIgnoreCase("idpersona") && 
                                   !col.equalsIgnoreCase("primerapellido") && 
                                   !col.equalsIgnoreCase("segundoapellido") && 
                                   !col.equalsIgnoreCase("primernombre") && 
                                   !col.equalsIgnoreCase("idrubro") && 
                                   !col.equalsIgnoreCase("idmunicipio") &&
                                   !col.equalsIgnoreCase("carnet")) 
                    .forEach(targetColumns::add);
            }
        };

        addFilteredColumns.accept(columnsrep, columns);
        addFilteredColumns.accept(columnsper, columns);
        addFilteredColumns.accept(columnsemp, columns);
        addFilteredColumns.accept(columnsrub, columns);
        addFilteredColumns.accept(columnsmun, columns);

        try {
            List<Empresas> empresas = iEmpresasAod.obtenerEmpresasDinamico(parametros);
            //System.out.println("Columnas para excel: " + columns.toString());
            data = iEmpresasRep.datosXLS(empresas, columns);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }
    
}
