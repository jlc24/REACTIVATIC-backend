package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.ILocalidadesAod;
import bo.sddpi.reactivatic.modulos.entidades.Localidades;

@RestController
@RequestMapping("/apirest/localidades")
public class LocalidadesCtrl {

    @Autowired
    ILocalidadesAod iLocalidadesAod;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "id", defaultValue = "0") Long id) {
        List<Localidades> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iLocalidadesAod.datos(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Localidades>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Localidades dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iLocalidadesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Localidades>(dato, HttpStatus.OK);
    }

    @GetMapping("/municipio/{id}")
    ResponseEntity<?> localidades(@PathVariable Long id){
        List<Localidades> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iLocalidadesAod.listaLocalidades(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Localidades>>(datos, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Localidades dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarlocalidad = iLocalidadesAod.verificarlocalidad(dato.getLocalidad());
        if (verificarlocalidad != null) {
            mensajes.put("mensaje", "La localidad ya se encuentra registrada en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                iLocalidadesAod.adicionar(dato);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Localidad registrada exitosamente en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Localidades dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarlocalidad = iLocalidadesAod.verificarlocalidad(dato.getLocalidad());
        if (verificarlocalidad != null) {
            mensajes.put("mensaje", "La localidad ya se encuentra registrada en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                iLocalidadesAod.modificar(dato);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @GetMapping("/l/{id}")
    ResponseEntity<?> datosl(@PathVariable Long id) {
        List<Localidades> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iLocalidadesAod.datosl(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Localidades>>(datos, HttpStatus.OK);
    }

    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@RequestBody Localidades localidad){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iLocalidadesAod.cambiarestado(localidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificaco el estado del rubro");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

}
