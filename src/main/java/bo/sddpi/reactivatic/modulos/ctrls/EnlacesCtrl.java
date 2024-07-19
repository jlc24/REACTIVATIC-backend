package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.IEnlacesAod;
import bo.sddpi.reactivatic.modulos.entidades.Enlaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apirest/enlaces")
public class EnlacesCtrl {
    
    @Autowired
    private IEnlacesAod iEnlacesAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Enlaces enlace = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlace = iEnlacesAod.dato(id);
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
    public ResponseEntity<?> listar() {
        List<Enlaces> enlaces;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            enlaces = iEnlacesAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(enlaces, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Enlaces enlace) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEnlacesAod.insertar(enlace);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace creado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Enlaces enlace) {
        Enlaces enlaceActual = iEnlacesAod.dato(id);
        Map<String, Object> mensajes = new HashMap<>();
        if (enlaceActual == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        try {
            enlaceActual.setIdcategoria(enlace.getIdcategoria());
            enlaceActual.setEnlace(enlace.getEnlace());
            enlaceActual.setRuta(enlace.getRuta());
            enlaceActual.setIconoenlace(enlace.getIconoenlace());
            enlaceActual.setOrden(enlace.getOrden());
            iEnlacesAod.actualizar(enlaceActual);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace actualizado exitosamente");
        mensajes.put("enlace", enlaceActual);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iEnlacesAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la eliminación en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Enlace eliminado exitosamente");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
