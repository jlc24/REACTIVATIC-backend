package bo.profesional.cesarnvf.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.IPersonasAod;
import bo.profesional.cesarnvf.modulos.aods.IRepresentantesAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosrolesAod;
import bo.profesional.cesarnvf.modulos.entidades.Personas;
import bo.profesional.cesarnvf.modulos.entidades.Usuarios;
import bo.profesional.cesarnvf.modulos.reportes.IUsuariosRep;

@RestController
@RequestMapping("/apirest/usuarios")
public class UsuariosCtrl {

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IUsuariosrolesAod iUsuariosrolesAod;

    @Autowired
    IPersonasAod iPersonasAod;

    @Autowired
    IRepresentantesAod iRepresentantesAod;

    @Autowired
    IUsuariosRep iUsuariosRep;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            datos = iUsuariosAod.datos(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Usuarios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iUsuariosAod.cantidad(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Usuarios dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iUsuariosAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Usuarios>(dato, HttpStatus.OK);
    }


    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Usuarios dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iUsuariosAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Personas persona = iPersonasAod.infoadicional(id);
            iUsuariosrolesAod.eliminar(id);
            iUsuariosAod.eliminar(id);
            iPersonasAod.eliminar(persona.getIdpersona());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping(value = "/rep/{id}")
    ResponseEntity<?> eliminarrep(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Personas persona = iPersonasAod.infoadicional(id);
            iUsuariosrolesAod.eliminar(id);
            iUsuariosAod.eliminar(id);
            iRepresentantesAod.eliminar(persona.getIdpersona());
            iPersonasAod.eliminar(persona.getIdpersona());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping(value = "/cambiarclave")
    ResponseEntity<?> cambiarclave(@RequestBody Usuarios usuario) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            usuario.setIdusuario(Long.parseLong(auth.getName()));
            iUsuariosAod.cambiarclave(usuario);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/datosrep")
    ResponseEntity<?> datosrep(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            datos = iUsuariosAod.datosrep(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Usuarios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidadrep")
    ResponseEntity<?> cantidadrep(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iUsuariosAod.cantidadrep(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/datosXLS", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> datosXLS(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iUsuariosAod.datosrepo(buscar);
            data = iUsuariosRep.datosXLS(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/datosPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<?> datosPDF(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iUsuariosAod.datosrepo(buscar);
            data = iUsuariosRep.datosPDF(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

}