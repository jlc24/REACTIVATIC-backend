package bo.sddpi.reactivatic.modulos.ctrls;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.IBeneficiosAod;
import bo.sddpi.reactivatic.modulos.aods.IBeneficiosempresasAod;
import bo.sddpi.reactivatic.modulos.aods.IEmpresasAod;
import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.entidades.Beneficiosempresas;
import bo.sddpi.reactivatic.modulos.reportes.IPlanillasRep;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/apirest/beneficiosempresas")
public class BeneficiosempresasCtrl {

    @Autowired
    private IBeneficiosempresasAod iBeneficiosempresasAod;

    @Autowired
    private IBeneficiosAod iBeneficiosAod;

    @Autowired
    private IEmpresasAod iEmpresasAod;

    @Autowired
    private IPlanillasRep iPlanillasRep;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "5") Integer cantidad,
            @RequestParam(value = "beneficio", required = true) Integer beneficio) {
        List<Beneficiosempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();

        if (beneficio == null || beneficio <= 0) {
            mensajes.put("mensaje", "El parámetro 'beneficio' es obligatorio y debe ser un valor positivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        int nropagina;
        Long id = Long.valueOf(beneficio);

        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            datos = iBeneficiosempresasAod.datos(id, buscar, cantidad, nropagina);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Beneficiosempresas>>(datos, HttpStatus.OK);
    }

    @GetMapping("/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                               @RequestParam(value = "beneficio", required = true) Integer beneficio){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();

        if (beneficio == null || beneficio <= 0) {
            mensajes.put("mensaje", "El parámetro 'beneficio' es obligatorio y debe ser un valor positivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long id = Long.valueOf(beneficio);
        try {
            cantidad = iBeneficiosempresasAod.cantidad(id, buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }
    @GetMapping("/l/{id}")
    ResponseEntity<?> datoslista(@PathVariable Long id){
        List<Beneficiosempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();

        if (id == null || id <= 0) {
            mensajes.put("mensaje", "El parámetro 'beneficio' es obligatorio y debe ser un valor positivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            datos = iBeneficiosempresasAod.datoslista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Beneficiosempresas>>(datos, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Map<String, Object> datos, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        if (!datos.containsKey("idrepresentante") || !datos.containsKey("idbeneficio")) {
            mensajes.put("mensaje", "Faltan parámetros necesarios (idrepresentante e idbeneficio).");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        }
    
        Long idrepresentante;
        Long idbeneficio;
        try {
            idrepresentante = Long.valueOf((Integer) datos.get("idrepresentante"));
            idbeneficio = Long.valueOf((Integer) datos.get("idbeneficio"));
        } catch (ClassCastException e) {
            mensajes.put("mensaje", "Los parámetros 'idrepresentante' e 'idbeneficio' deben ser números.");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            Long idempresa = iEmpresasAod.empresa(idrepresentante);

            if (idempresa == null) {
                mensajes.put("mensaje", "No se encontró la empresa asociada al representante con id: " + idrepresentante);
                return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
            }

            Beneficiosempresas beneficioempresa = new Beneficiosempresas();
            beneficioempresa.setIdempresa(idempresa);
            beneficioempresa.setIdbeneficio(idbeneficio);

            iBeneficiosempresasAod.adicionar(beneficioempresa);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El beneficioempresa ha sido creado");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @DeleteMapping
    ResponseEntity<?> eliminar(@Valid @RequestBody Map<String, Object> datos, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        if (!datos.containsKey("idrepresentante") || !datos.containsKey("idbeneficio")) {
            mensajes.put("mensaje", "Faltan parámetros necesarios (idrepresentante e idbeneficio).");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        }
    
        Long idrepresentante;
        Long idbeneficio;
        try {
            idrepresentante = Long.valueOf((Integer) datos.get("idrepresentante"));
            idbeneficio = Long.valueOf((Integer) datos.get("idbeneficio"));
        } catch (ClassCastException e) {
            mensajes.put("mensaje", "Los parámetros 'idrepresentante' e 'idbeneficio' deben ser números.");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            Long idempresa = iEmpresasAod.empresa(idrepresentante);

            if (idempresa == null) {
                mensajes.put("mensaje", "No se encontró la empresa asociada al representante con id: " + idrepresentante);
                return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
            }

            Beneficiosempresas beneficioempresa = new Beneficiosempresas();
            beneficioempresa.setIdempresa(idempresa);
            beneficioempresa.setIdbeneficio(idbeneficio);

            iBeneficiosempresasAod.eliminar(beneficioempresa);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Vase de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El beneficioempresa ha sido eliminado con exito");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/cantidad/{id}")
    ResponseEntity<?> cantidadbeneficio(@PathVariable Long id){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iBeneficiosempresasAod.cantidadBeneficio(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/beneficios")
    ResponseEntity<?> empresabeneficios(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                        @RequestParam(value = "empresa", required = true) Long empresa) {
        List<Beneficiosempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();

        if (empresa == null || empresa <= 0) {
            mensajes.put("mensaje", "El parámetro 'empresa' es obligatorio y debe ser un valor positivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            datos = iBeneficiosempresasAod.empresabeneficios(empresa, buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Beneficiosempresas>>(datos, HttpStatus.OK);
    }

    @GetMapping("/beneficios/cantidad")
    ResponseEntity<?> cantidadbeneficio(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                        @RequestParam(value = "empresa", required = true) Long empresa){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iBeneficiosempresasAod.cantidadbe(empresa, buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/planillaregistro/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<?> planillaReg(@PathVariable Long id){
        Long beneficio = id;

        byte[] dato = null;
        List<Beneficiosempresas> datos = null;
        Beneficios datobeneficio = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iBeneficiosempresasAod.planillareg(beneficio);
            datobeneficio = iBeneficiosAod.dato(beneficio);
            dato = iPlanillasRep.planillaRegistroPDF(datos, datobeneficio);

            if (datos == null || datos.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron datos para generar el PDF.");
            }
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(dato, HttpStatus.OK);
    }

    @GetMapping(value = "/planillainscripcion/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<?> planillaInsc(@PathVariable Long id){
        Long beneficio = id;

        byte[] dato = null;
        Beneficios datobeneficio = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datobeneficio = iBeneficiosAod.dato(beneficio);
            dato = iPlanillasRep.planillaInscripcionPDF(datobeneficio);

            if (datobeneficio == null ) {
                throw new IllegalArgumentException("No se encontraron datos para generar el PDF.");
            }
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(dato, HttpStatus.OK);
    }

    @GetMapping(value = "/planillaXLS/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> planillaRegXLS(@PathVariable Long id) {
        Long beneficio = id;

        byte[] dato = null;
        List<Beneficiosempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        
        try {
            datos = iBeneficiosempresasAod.planillareg(beneficio);
            dato = iPlanillasRep.planillaRegistroXLS(datos);
            
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(dato, HttpStatus.OK);
    }
    
}
