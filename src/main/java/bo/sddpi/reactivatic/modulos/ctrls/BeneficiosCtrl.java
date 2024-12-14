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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import bo.sddpi.reactivatic.modulos.aods.IBeneficiosAod;
import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/apirest/beneficios")
public class BeneficiosCtrl {
    
    @Autowired
    private IBeneficiosAod ibeneficiosAod;

    @Autowired
    private ISubirarchivosServ iSubirarchivosServ;


    //BUSCAR BENEFICIOS->PAGINACION->CANTIDAD
    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad,
            @RequestParam(value = "rol", defaultValue = "") String rol) {
        List<Beneficios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina;
        Long id = null;
        try {
            if ((pagina-1) * cantidad < 0) {
                nropagina = 0;
            }else{
                nropagina = (pagina-1) * cantidad;
            }
            if (rol.equals("admin")) {
                id = null;
            }else{
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                id = Long.parseLong(auth.getName());
            }
            datos = ibeneficiosAod.buscar(id, buscar, cantidad, nropagina);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en al base de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Beneficios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = ("/cantidad"))
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                @RequestParam(value = "rol", defaultValue = "") String rol){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long id = null;
        try{
            if (rol.equals("admin")) {
                id = null;
            }else{
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                id = Long.parseLong(auth.getName());
            }
            cantidad = ibeneficiosAod.cantidad(id, buscar);
        }catch(DataAccessException e){
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    //LISTA DE BENEFICIOS
    @GetMapping("/l")
    ResponseEntity<?> listar() {
        List<Beneficios> beneficios;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            beneficios = ibeneficiosAod.listar();
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
            beneficio = ibeneficiosAod.dato(id);
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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idusuario = Long.parseLong(auth.getName());
            beneficio.setIdusuario(idusuario);
            ibeneficiosAod.insertar(beneficio);
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
            ibeneficiosAod.actualizar(beneficio);
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
            ibeneficiosAod.cambiarestado(beneficio);
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
            ibeneficiosAod.eliminar(beneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Vase de datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El beneficio ha sido eliminado con exito");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PostMapping(value="/upload")
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("id") Long id,
            @RequestParam("tipo") String tipo,
            @RequestParam("archivo") MultipartFile archivo) {

        Map<String, Object> mensajes = new HashMap<>();

        if (archivo.isEmpty()) {
            mensajes.put("mensaje", "No se ha seleccionado ningún archivo.");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            iSubirarchivosServ.uploadimagen(id, archivo, tipo);
        } catch (NumberFormatException e) {
            mensajes.put("mensaje", "El ID de usuario no es válido.");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException  e) {
            mensajes.put("mensaje", "Error al procesar el archivo: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo cargar el archivo: " + archivo.getOriginalFilename() + ". Error: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        mensajes.put("mensaje", "Se cargó el archivo con éxito: " + archivo.getOriginalFilename());
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/downloadimage")
    public ResponseEntity<?> downloadImage(@RequestParam("id") Long id, @RequestParam("tipo") String tipo){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            List<Map<String, String>> imagenes = iSubirarchivosServ.downloadimagen(id, tipo);

            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            mensajes.put("error", "Error al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajes);
        }
    }

    @GetMapping("/negocios")
    ResponseEntity<?> negocio(){
        Beneficios beneficio = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            beneficio = ibeneficiosAod.negocios();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(beneficio, HttpStatus.OK);
    }
    
}
