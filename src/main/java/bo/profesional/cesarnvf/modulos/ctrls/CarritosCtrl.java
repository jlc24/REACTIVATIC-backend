package bo.profesional.cesarnvf.modulos.ctrls;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.ICarritosAod;
import bo.profesional.cesarnvf.modulos.entidades.Carritos;

@RestController
@RequestMapping("/carritos")
public class CarritosCtrl {

    @Autowired
    ICarritosAod iCarritosAod;

    @GetMapping("/l")
    ResponseEntity<?> datosl(@RequestParam(value = "idcliente", defaultValue = "") Long idcliente) {
        List<Carritos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iCarritosAod.datosl(idcliente);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Carritos>>(datos, HttpStatus.OK);
    }


    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Carritos dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iCarritosAod.adicionar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/cantidadcarrito")
    ResponseEntity<?> cantidadcarrito(@RequestParam(value = "idcliente", defaultValue = "") Long idcliente) {
        Carritos dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iCarritosAod.cantidadcarrito(idcliente);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Carritos>(dato, HttpStatus.OK);
    }

    @DeleteMapping("/{idcliente}/{idproducto}")
    ResponseEntity<?> eliminar(@PathVariable Long idcliente, @PathVariable Long idproducto) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iCarritosAod.eliminar(idcliente, idproducto);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

}
