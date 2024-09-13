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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.ICarritosAod;
import bo.sddpi.reactivatic.modulos.entidades.Carritos;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@RestController
@RequestMapping("/carritos")
public class CarritosCtrl {

    @Autowired
    ICarritosAod iCarritosAod;

    @Autowired
    ISubirarchivosServ iSubirarchivosServ;

    @GetMapping("/l")
    ResponseEntity<?> datosl(@RequestParam(value = "idcliente", defaultValue = "") Long idcliente) {
        List<Carritos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iCarritosAod.datosl(idcliente);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Carritos>>(datos, HttpStatus.OK);
    }

    @GetMapping("/atributosproducto/{id}")
    ResponseEntity<?> atributos(@PathVariable Long id){
        Carritos atributos = null;
        String nombreimagen = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            atributos = iCarritosAod.atributosProducto(id);
            nombreimagen = iSubirarchivosServ.descargarnombreimagen(id.toString());
            if (atributos != null) {
                atributos.setImagen(nombreimagen);
            }
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (atributos == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(atributos, HttpStatus.OK);
    }

    @GetMapping("/descargarnombreimagen/{id}")
    ResponseEntity<?> nombreimagen(@PathVariable Long id){
        String nombre = null;
        Map<String, String> nombrejson = new HashMap<>();
        Map<String, Object> mensajes = new HashMap<>();
        try {
            nombre = iSubirarchivosServ.descargarnombreimagen(id.toString());
            nombrejson.put("imagen", nombre); 
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(nombrejson, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Carritos dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Carritos carrito = iCarritosAod.dato(dato);

            if (carrito != null) {
                Long newcantidad = carrito.getCantidad() + dato.getCantidad(); 
                carrito.setCantidad(newcantidad);
                iCarritosAod.actualizar(carrito);
            }else{
                iCarritosAod.adicionar(dato);

            }
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/cantidadcarrito")
    ResponseEntity<?> cantidadcarrito(@RequestParam(value = "idcliente", defaultValue = "") Long idcliente) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iCarritosAod.cantidadcarrito(idcliente);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iCarritosAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

}
