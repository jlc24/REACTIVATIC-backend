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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.ICargosAod;
import bo.sddpi.reactivatic.modulos.entidades.Cargos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/apirest/cargos")
public class CargosCtrl {

    @Autowired
    private ICargosAod icargosAod;

    @GetMapping("/{id}")
    ResponseEntity<?> dato(@PathVariable Long id){
        Cargos cargo = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cargo = icargosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cargo == null) {
            mensajes.put("mensaje", "El id: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cargo, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad){
        List<Cargos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            datos = icargosAod.datos(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Cargos>>(datos, HttpStatus.OK);
    }
    
    @GetMapping("/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = icargosAod.cantidad(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }
    
    @GetMapping("/rol/{id}")
    ResponseEntity<?> listar(@PathVariable Long id){
        List<Cargos> cargos;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cargos = icargosAod.listar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Cargos>>(cargos, HttpStatus.OK);
    }
    
    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Cargos cargo, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icargosAod.insertar(cargo);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Cargo creado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }
    
    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Cargos cargo, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icargosAod.actualizar(cargo);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Cargo actualizado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
