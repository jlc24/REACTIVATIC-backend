package bo.sddpi.reactivatic.modulos.ctrls;

import bo.sddpi.reactivatic.modulos.aods.IRolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apirest/roles")
public class RolesCtrl {
    
    @Autowired
    private IRolesAod irolesAod;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Roles rol = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            rol = irolesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (rol == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rol, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Roles> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if((pagina-1) * cantidad < 0){
                nropagina = 0;
            }else {
                nropagina = (pagina - 1) * cantidad;
            }
            datos = irolesAod.datos(buscar, nropagina, cantidad);
        }catch(DataAccessException e){   
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Roles>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar){
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try{
            cantidad = irolesAod.cantidad(buscar);
        }catch(DataAccessException e){
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/l")
    public ResponseEntity<?> listar() {
        List<Roles> roles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            roles = irolesAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/admin")
    ResponseEntity<?> listaradmin() {
        List<Roles> roles = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            roles = irolesAod.listaradmin();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Roles>>(roles, HttpStatus.OK);
    }

    @GetMapping("/sddpi")
    public ResponseEntity<?> listarsddpi() {
        List<Roles> roles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            roles = irolesAod.listarsddpi();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/reactivatic")
    public ResponseEntity<?> listarreactivatic() {
        List<Roles> roles;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            roles = irolesAod.listarreactivatic();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Roles roles) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            irolesAod.insertar(roles);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la inserción en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El rol ha sido creado con éxito");
        mensajes.put("rol", roles);
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Roles roles) {
        Roles rolActual = irolesAod.dato(id);
        Map<String, Object> mensajes = new HashMap<>();
        if (rolActual == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }
        try {
            rolActual.setRol(roles.getRol());
            irolesAod.actualizar(rolActual);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al actualizar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El rol ha sido actualizado con éxito");
        mensajes.put("rol", rolActual);
        return new ResponseEntity<>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            irolesAod.eliminar(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al eliminar en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "El rol ha sido eliminado con éxito");
        return new ResponseEntity<>(mensajes, HttpStatus.NO_CONTENT);
    }
}
