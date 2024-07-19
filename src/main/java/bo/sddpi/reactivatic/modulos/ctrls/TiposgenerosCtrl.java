package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.ITiposgenerosAod;
import bo.sddpi.reactivatic.modulos.entidades.Tiposgeneros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/apirest/tiposgeneros")
public class TiposgenerosCtrl {
    
    @Autowired
    private ITiposgenerosAod itiposgenerosAod;
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Tiposgeneros tipoGenero = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tipoGenero = itiposgenerosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (tipoGenero == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tipoGenero, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Tiposgeneros> tiposGeneros;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tiposGeneros = itiposgenerosAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tiposGeneros, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Tiposgeneros tiposgeneros) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itiposgenerosAod.insertar(tiposgeneros);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de género ha sido creado con éxito");
        mensajes.put("tipoGenero", tiposgeneros);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Tiposgeneros tiposgeneros) {
        Tiposgeneros tipoGeneroActual = itiposgenerosAod.dato(id);
        Map<String, Object> mensajes = new HashMap<>();
        if (tipoGeneroActual == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        try {
            tipoGeneroActual.setTipogenero(tiposgeneros.getTipogenero());
            itiposgenerosAod.actualizar(tipoGeneroActual);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de género ha sido actualizado con éxito");
        mensajes.put("tipoGenero", tipoGeneroActual);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itiposgenerosAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de género ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
}
