package bo.sddpi.reactivatic.modulos.ctrls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.INegociosAod;
import bo.sddpi.reactivatic.modulos.entidades.Municipios;
import bo.sddpi.reactivatic.modulos.entidades.Negocios;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/apirest/negocios")
public class NegociosCtrl {

    @Autowired
    INegociosAod inegociosaod;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "beneficio") Integer beneficio) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        //Long idbeneficio = null;
        try {
            if (beneficio == null || beneficio == 0) {
                mensajes.put("mensaje", "El parámetro 'beneficio' es obligatorio y debe ser mayor que 0.");
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
            }

            //idbeneficio = Long.parse(beneficio);

            // if (idbeneficio == 0) {
            //     mensajes.put("mensaje", "El parámetro 'beneficio' debe ser mayor que 0.");
            //     return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
            // }
            datos = inegociosaod.datos(buscar, beneficio.longValue());
        }  catch (NumberFormatException e) {
            mensajes.put("mensaje", "El parámetro 'beneficio' debe ser un número válido.");
            mensajes.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }  catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                               @RequestParam(value = "beneficio") Integer beneficio) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = inegociosaod.cantidad(buscar, beneficio.longValue());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id, @RequestParam(value = "beneficio", defaultValue = "0") String beneficio) {
        Negocios dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long idbeneficio = null;
        try {
            idbeneficio = Long.parseLong(beneficio);
            dato = inegociosaod.dato(id, idbeneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Negocios>(dato, HttpStatus.OK);
    }

    @GetMapping("/fechas")
    ResponseEntity<?> fechas(@RequestParam(value = "empresa", defaultValue = "0") String empresa,
                             @RequestParam(value = "beneficio", defaultValue = "0") String beneficio) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long idbeneficio = null;
        Long idempresa = null;
        try {
            idbeneficio = Long.parseLong(beneficio);
            idempresa = Long.parseLong(empresa);
            
            datos = inegociosaod.fechas(idempresa, idbeneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }
    
    @GetMapping("/horas")
    ResponseEntity<?> horas(@RequestParam(value = "empresa", defaultValue = "0") String empresa,
                             @RequestParam(value = "beneficio", defaultValue = "0") String beneficio,
                             @RequestParam(value = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long idbeneficio = null;
        Long idempresa = null;
        
        try {
            idbeneficio = Long.parseLong(beneficio);
            idempresa = Long.parseLong(empresa);
            
            datos = inegociosaod.horas(fecha, idempresa, idbeneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }

    @GetMapping("/mesas")
    ResponseEntity<?> mesas(@RequestParam(value = "hora") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
                            @RequestParam(value = "beneficio", defaultValue = "0") String beneficio,
                            @RequestParam(value = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long idbeneficio = null;
        
        try {
            idbeneficio = Long.parseLong(beneficio);
            
            datos = inegociosaod.mesas(idbeneficio, fecha, hora);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Negocios dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            inegociosaod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/id")
    ResponseEntity<?> eliminar(@PathVariable Long id){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            inegociosaod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El precio ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
    
}
