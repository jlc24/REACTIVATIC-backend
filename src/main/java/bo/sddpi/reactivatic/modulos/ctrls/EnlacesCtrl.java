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

import bo.sddpi.reactivatic.modulos.aods.IEnlacesAod;
import bo.sddpi.reactivatic.modulos.entidades.Enlaces;

@RestController
@RequestMapping("/apirest/enlaces")
public class EnlacesCtrl {
    
    @Autowired
    private IEnlacesAod ienlacesAod;

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        Enlaces enlace = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlace = ienlacesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (enlace == null) {
            mensajes.put("mensaje", "El id: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enlace, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad){
        List<Enlaces> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try{
            if((pagina-1)*cantidad < 0){
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            datos = ienlacesAod.datos(buscar, nropagina, cantidad);
        }catch(DataAccessException e){
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Enlaces>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = ienlacesAod.cantidad(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/categoria/{id}")
    ResponseEntity<?> listar(@PathVariable Long id) {
        List<Enlaces> enlaces;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlaces = ienlacesAod.listar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(enlaces, HttpStatus.OK);
    }

    @GetMapping(value = "/enlacesroles/{id}")
    public ResponseEntity<?> listarEnlaces(@PathVariable Long id) {
        List<Enlaces> enlacesRoles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlacesRoles = ienlacesAod.listarEnlaces(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(enlacesRoles, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Enlaces enlace, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarenlace = ienlacesAod.verificarenlace(enlace.getEnlace());
        Long verificarruta = ienlacesAod.verificarenlaceruta(enlace.getRuta());
        if (verificarenlace != null) {
            mensajes.put("mensaje", "El enlace ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else if (verificarruta != null) {
            mensajes.put("mensaje", "La ruta del enlace ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else {
            try {
                ienlacesAod.insertar(enlace);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
                mensajes.put("error",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Enlace creado exitosamente");
            return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
        }
    }

    @PutMapping
    ResponseEntity<?> actualizar(@Valid @RequestBody Enlaces enlace, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarenlace = ienlacesAod.verificarenlace(enlace.getEnlace());
        Long verificarruta = ienlacesAod.verificarenlaceruta(enlace.getRuta());
        if (verificarenlace != null) {
            mensajes.put("mensaje", "El enlace ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else if (verificarruta != null) {
            mensajes.put("mensaje", "La ruta del enlace ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else {
            try {
                ienlacesAod.actualizar(enlace);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Enlace actualizado exitosamente");
            return new ResponseEntity<>(mensajes, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            ienlacesAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la eliminación en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace eliminado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
