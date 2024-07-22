package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.ITiposextensionesAod;
import bo.sddpi.reactivatic.modulos.entidades.Tiposextensiones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apirest/tiposextensiones")
public class TiposextensionesCtrl {

    @Autowired
    private ITiposextensionesAod itiposextensionesAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Tiposextensiones tipoExtension = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tipoExtension = itiposextensionesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (tipoExtension == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tipoExtension, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Tiposextensiones> tipoExtensiones;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tipoExtensiones = itiposextensionesAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tipoExtensiones, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Tiposextensiones tiposextensiones) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itiposextensionesAod.insertar(tiposextensiones);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de extensión ha sido creado con éxito");
        mensajes.put("tipoExtension", tiposextensiones);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Tiposextensiones tiposextensiones) {
        Tiposextensiones tipoExtensionActual = itiposextensionesAod.dato(id);
        Map<String, Object> mensajes = new HashMap<>();
        if (tipoExtensionActual == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        try {
            tipoExtensionActual.setTipoextension(tiposextensiones.getTipoextension());
            tipoExtensionActual.setSigla(tiposextensiones.getSigla());
            itiposextensionesAod.actualizar(tipoExtensionActual);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de extensión ha sido actualizado con éxito");
        mensajes.put("tipoExtension", tipoExtensionActual);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itiposextensionesAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de extensión ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
}
