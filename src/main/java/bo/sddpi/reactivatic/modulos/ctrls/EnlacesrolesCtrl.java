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

import bo.sddpi.reactivatic.modulos.aods.IEnlacesrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Enlaces;
import bo.sddpi.reactivatic.modulos.entidades.Enlacesroles;

@RestController
@RequestMapping("/apirest/enlacesroles")
public class EnlacesrolesCtrl {
    @Autowired
    private IEnlacesrolesAod ienlacesrolesAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEnlaceRol(@PathVariable Long id) {
        Enlacesroles enlaceRol;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlaceRol = ienlacesrolesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (enlaceRol == null) {
            mensajes.put("mensaje", "El id: " + id + " no existe en la Base de Datos");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(enlaceRol, HttpStatus.OK);
    }

    @GetMapping(value = "/roles/{id}")
    public ResponseEntity<?> listarRoles(@PathVariable Long id) {
        List<Enlacesroles> enlacesRoles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlacesRoles = ienlacesrolesAod.listarRoles(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(enlacesRoles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearEnlaceRol(@Valid @RequestBody Enlacesroles enlaceRol, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        
        try {
            Long existe = ienlacesrolesAod.verificar(enlaceRol.getIdrol(), enlaceRol.getIdenlace());
            if (existe != null) {
                mensajes.put("mensaje", "Error: El Enlace-Rol ya existe en la base de datos.");
                return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
            }
            ienlacesrolesAod.insertar(enlaceRol);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol creado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> actualizarEnlaceRol(@Valid @RequestBody Enlacesroles enlaceRol, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            
            ienlacesrolesAod.actualizar(enlaceRol);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la actualización en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol actualizado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarEnlaceRol(@Valid @RequestBody Enlacesroles enlacesroles, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Long existe = ienlacesrolesAod.verificar(enlacesroles.getIdrol(), enlacesroles.getIdenlace());
            if (existe == null) {
                mensajes.put("mensaje", "Error: El Enlace-Rol no existe en la base de datos.");
                return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
            }
            //Long existe = ienlacesrolesAod.verificar(enlacesroles.getIdrol(), enlacesroles.getIdenlace());
            enlacesroles.setIdenlacerol(existe);
            ienlacesrolesAod.eliminar(enlacesroles);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la eliminación en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol eliminado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
