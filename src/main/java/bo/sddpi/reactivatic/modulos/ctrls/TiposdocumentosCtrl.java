package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.ITiposdocumentosAod;
import bo.sddpi.reactivatic.modulos.entidades.Tiposdocumentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/apirest/tiposdocumentos")
public class TiposdocumentosCtrl {
    
    @Autowired
    private ITiposdocumentosAod itiposDocumentosAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Tiposdocumentos tipoDocumento = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tipoDocumento = itiposDocumentosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (tipoDocumento == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tipoDocumento, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Tiposdocumentos> tiposDocumentos;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tiposDocumentos = itiposDocumentosAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tiposDocumentos, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Tiposdocumentos tiposdocumentos, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            itiposDocumentosAod.insertar(tiposdocumentos);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de documento ha sido creado con éxito");
        mensajes.put("tipoDocumento", tiposdocumentos);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Tiposdocumentos tiposdocumentos) {
        Tiposdocumentos tipoDocumentoActual = itiposDocumentosAod.dato(id);
        Map<String, Object> mensajes = new HashMap<>();
        if (tipoDocumentoActual == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        try {
            tipoDocumentoActual.setTipodocumento(tiposdocumentos.getTipodocumento());
            tipoDocumentoActual.setDocumento(tiposdocumentos.getDocumento());
            itiposDocumentosAod.actualizar(tipoDocumentoActual);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de documento ha sido actualizado con éxito");
        mensajes.put("tipoDocumento", tipoDocumentoActual);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            itiposDocumentosAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El tipo de documento ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
}
