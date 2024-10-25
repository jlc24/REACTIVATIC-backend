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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.IAsistenciasempresasAod;
import bo.sddpi.reactivatic.modulos.entidades.Asistenciasempresas;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/apirest/asistenciasempresas")
public class AsistenciasempresasCtrl {

    @Autowired
    IAsistenciasempresasAod iAsistenciasempresasAod;

    @GetMapping("/fechas/{id}")
    ResponseEntity<?> fechas(@PathVariable Long id){
        List<Asistenciasempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iAsistenciasempresasAod.fechas(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Asistenciasempresas>>(datos, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    ResponseEntity<?> datos(@PathVariable Long id){
        List<Asistenciasempresas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iAsistenciasempresasAod.datos(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Asistenciasempresas>>(datos, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> modificarasistencia(@Valid @RequestBody Asistenciasempresas asistencia, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iAsistenciasempresasAod.modificarasistencia(asistencia);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }
}
