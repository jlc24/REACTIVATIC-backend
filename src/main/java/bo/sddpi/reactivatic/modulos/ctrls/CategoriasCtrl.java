package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.ICategoriasAod;
import bo.sddpi.reactivatic.modulos.entidades.Categorias;

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
@RequestMapping("/apirest/categorias")
public class CategoriasCtrl {
    
    @Autowired
    private ICategoriasAod icategoriasAod;

    //BUSCAR CATEGORIAS->PAGINACION->CANTIDAD
    @GetMapping
    ResponseEntity<?> buscar(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Categorias> categorias = null;
        Map<String , Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else {
                nropagina = (pagina-1)*cantidad;
            }
            categorias = icategoriasAod.buscar(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Categorias>>(categorias, HttpStatus.OK);
    }

    //LISTA DE CATEGORIAS
    @GetMapping("/l")
    public ResponseEntity<?> listar() {
        List<Categorias> categorias;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            categorias = icategoriasAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }
    
    //VER
    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        Categorias categoria = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            categoria = icategoriasAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (categoria == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    //AGREGAR
    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Categorias categorias, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icategoriasAod.insertar(categorias);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La categoría ha sido creada con éxito");
        mensajes.put("categoria", categorias);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    //MODIFICAR
    @PutMapping
    public ResponseEntity<?> actualizar(@Valid @RequestBody Categorias categorias, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icategoriasAod.actualizar(categorias);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La categoría ha sido actualizada con éxito");
        mensajes.put("categoria", categorias);
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    //CAMBIAR ESTADO DE CATEGORIA
    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@Valid @RequestBody Categorias estado, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icategoriasAod.cambiarestado(estado);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Vase de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Cambio de estado actualizado");
        //mensajes.put("categoria", estado);
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    //ELIMINAR
    @DeleteMapping //("/{id}")
    public ResponseEntity<?> eliminar(@Valid @RequestBody Categorias categorias, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            icategoriasAod.eliminar(categorias);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La categoría ha sido eliminada con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }
}
