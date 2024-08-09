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

import bo.sddpi.reactivatic.modulos.aods.IComentariosAod;
import bo.sddpi.reactivatic.modulos.entidades.Comentarios;


@RestController
@RequestMapping("/apirest/comentarios")
public class ComentariosCtrl {
    
    @Autowired
    IComentariosAod iComentariosAod;

    //BUSCAR COMENTARIOS DE PRODUCTO->PAGINACION->CANTIDAD
    @GetMapping("/productos/{id}")
    ResponseEntity<?> buscarproducto(@PathVariable("id") Long idproducto,
            @RequestParam(value = "pagina", defaultValue = "") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad){
        List<Comentarios> comentarios = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            comentarios = iComentariosAod.buscarp(idproducto, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Comentarios>>(comentarios, HttpStatus.OK);
    }
    //BUSCAR COMENTARIOS DE PRODUCTO->PAGINACION->CANTIDAD
    @GetMapping("/cliente/{idproducto}/{idcliente}")
    ResponseEntity<?> buscarcliente(@PathVariable("idproducto") Long idproducto, @PathVariable("idcliente") Long idcliente,
            @RequestParam(value = "pagina", defaultValue = "") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad){
        List<Comentarios> comentarios = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            comentarios = iComentariosAod.buscarp(idproducto, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Comentarios>>(comentarios, HttpStatus.OK);
    }

    //AGREGAR COMENTARIO
    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Comentarios comentario, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iComentariosAod.adicionar(comentario);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La categoría ha sido creada con éxito");
        mensajes.put("comentario", comentario);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    //CAMBIAR ESTADO DE COMENTARIO
    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@Valid @RequestBody Comentarios estado, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iComentariosAod.cambiarestado(estado);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Vase de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Cambio de estado actualizado");
        //mensajes.put("comentario", estado);
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    //ELIMINAR
    @DeleteMapping //("/{id}")
    public ResponseEntity<?> eliminar(@Valid @RequestBody Comentarios comentario, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iComentariosAod.eliminar(comentario);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El Comentario ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
