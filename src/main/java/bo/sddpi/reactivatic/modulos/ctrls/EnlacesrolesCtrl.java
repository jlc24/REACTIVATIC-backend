package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.IEnlacesrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Enlacesroles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apirest/enlacesroles")
public class EnlacesrolesCtrl {
    @Autowired
    private IEnlacesrolesAod iEnlacesrolesAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEnlaceRol(@PathVariable Long id) {
        Enlacesroles enlaceRol;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlaceRol = iEnlacesrolesAod.dato(id);
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

    @GetMapping
    public ResponseEntity<?> listarEnlacesRoles() {
        List<Enlacesroles> enlacesRoles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlacesRoles = iEnlacesrolesAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(enlacesRoles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crearEnlaceRol(@RequestBody Enlacesroles enlaceRol) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEnlacesrolesAod.insertar(enlaceRol);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol creado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> actualizarEnlaceRol(@RequestBody Enlacesroles enlaceRol) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEnlacesrolesAod.actualizar(enlaceRol);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la actualización en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol actualizado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEnlaceRol(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEnlacesrolesAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la eliminación en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace-Rol eliminado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
