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
import org.springframework.web.bind.annotation.*;

import bo.sddpi.reactivatic.modulos.aods.ITamanosAod;
import bo.sddpi.reactivatic.modulos.entidades.Tamanos;

@RestController
@RequestMapping("apirest/tamanos")
public class TamanosCtrl {

    @Autowired
    ITamanosAod itamanosAod;

    @GetMapping("/{id}")
    ResponseEntity<?> tamanos(@PathVariable Long id){
        List<Tamanos> tamanos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tamanos = itamanosAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Tamanos>>(tamanos, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Tamanos tamano, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            itamanosAod.adicionar(tamano);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha adicionado correctamente el Precio.");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itamanosAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tamaño o talla ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
}
