package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import bo.sddpi.reactivatic.modulos.aods.IBeneficiosAod;
import bo.sddpi.reactivatic.modulos.entidades.Beneficios;

@RestController
@RequestMapping("/apirest/beneficios")
public class BeneficiosCtrl {
    
    @Autowired
    private IBeneficiosAod iBeneficiosAod;

    //BUSCAR BENEFICIOS->PAGINACION->CANTIDAD
    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Beneficios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            datos = iBeneficiosAod.buscar(buscar, cantidad, nropagina);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Beneficios>>(datos, HttpStatus.OK);
    }

    //LISTA DE BENEFICIOS
    @GetMapping("/l")
    ResponseEntity<?> listar() {
        List<Beneficios> beneficios;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            beneficios = iBeneficiosAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(beneficios, HttpStatus.OK);
    }

    //VER O EDITAR
    @GetMapping("/{id}")
    ResponseEntity<?> dato(@PathVariable Long id){
        Beneficios beneficio = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            beneficio = iBeneficiosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(beneficio, HttpStatus.OK);
    }

    //AGREGAR
    @PostMapping
    ResponseEntity<?> crear(@Valid @RequestBody Beneficios beneficio, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iBeneficiosAod.insertar(beneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La beneficio ha sido creado");
        mensajes.put("beneficios", beneficio);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    //MODIFICAR
    @PutMapping
    ResponseEntity<?> actualizar(@Valid @RequestBody Beneficios beneficio, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iBeneficiosAod.actualizar(beneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "La categoría ha sido actualizada con éxito");
        mensajes.put("beneficio", beneficio);
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    //CAMBIAR ESTADO DEBENEFICIOS
    @PutMapping("/cambiarestado")
    ResponseEntity<?> cambiarestado(@Valid @RequestBody Beneficios beneficio, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iBeneficiosAod.cambiarestado(beneficio);
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
    @DeleteMapping
    ResponseEntity<?> eliminar(@Valid @RequestBody Beneficios beneficio, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Vase de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El beneficio ha sido eliminado con exito");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }
}
