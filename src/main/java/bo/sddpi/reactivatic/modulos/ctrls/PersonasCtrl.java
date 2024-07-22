package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Representantes;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.entidades.Usuariosroles;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@RestController
@RequestMapping("/apirest/personas")
@PreAuthorize("isAuthenticated()")
public class PersonasCtrl {

    @Autowired
    IPersonasAod iPersonasAod;

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IUsuariosrolesAod iUsuariosrolesAod;

    @Autowired
    IRepresentantesAod iRepresentantesAod;

    @Autowired
    ISubirarchivosServ iSubirarchivosServ;

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Personas dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iPersonasAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato==null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Personas>(dato, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<?> adicionar4(@Valid @RequestBody Personas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Usuarios usuarionuevo = new Usuarios();
            Personas personanuevo = new Personas();
            Usuariosroles usuariorolnuevo = new Usuariosroles();
            personanuevo.setPrimerapellido(dato.getPrimerapellido());
            personanuevo.setSegundoapellido(dato.getSegundoapellido());
            personanuevo.setPrimernombre(dato.getPrimernombre());
            //personanuevo.setSegundonombre(dato.getSegundonombre());
            //personanuevo.setFechanacimiento(dato.getFechanacimiento());
            personanuevo.setDip(dato.getDip());
            personanuevo.setDireccion(dato.getDireccion());
            personanuevo.setTelefono(dato.getTelefono());
            personanuevo.setCelular(dato.getCelular());
            personanuevo.setCorreo(dato.getCorreo());
            personanuevo.setEstado(true);
            iPersonasAod.adicionar(personanuevo);
            usuarionuevo.setUsuario(dato.getDip());
            usuarionuevo.setClave(dato.getDip());
            usuarionuevo.setEstado(true);
            usuarionuevo.setIdpersona(personanuevo.getIdpersona());
            iUsuariosAod.adicionar(usuarionuevo);
            usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
            usuariorolnuevo.setIdrol(4L);
            iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Personas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            iPersonasAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }


    @GetMapping(value = "/perfil")
    ResponseEntity<?> perfil() {
        Personas dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            dato = iPersonasAod.infoadicional(Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Personas>(dato, HttpStatus.OK);
    }

    @PostMapping(value="/cargar")
    ResponseEntity<?> cargar(@RequestParam("archivo") MultipartFile archivo) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            iSubirarchivosServ.cargarimagen(Long.parseLong(auth.getName()), archivo);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo cargar el archivo: "+archivo.getOriginalFilename()+"!!!");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se cargo el archivo con éxito: "+archivo.getOriginalFilename());
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping(value="/descargar/{idusuario}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargas(@PathVariable Long idusuario) {
        Map<String, Object> mensajes = new HashMap<>();
        Resource archivo = null;
        try {
            //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            archivo = iSubirarchivosServ.descargarimagen(idusuario.toString());
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo descargar el archivo");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @PostMapping("/adicionarrep")
    ResponseEntity<?> adicionar2(@Valid @RequestBody Personas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Usuarios usuarionuevo = new Usuarios();
            Personas personanuevo = new Personas();
            Representantes representantenuevo = new Representantes();
            Usuariosroles usuariorolnuevo = new Usuariosroles();
            personanuevo.setPrimerapellido(dato.getPrimerapellido());
            personanuevo.setSegundoapellido(dato.getSegundoapellido());
            personanuevo.setPrimernombre(dato.getPrimernombre());
            //personanuevo.setSegundonombre(dato.getSegundonombre());
            //personanuevo.setFechanacimiento(dato.getFechanacimiento());
            personanuevo.setDip(dato.getDip());
            personanuevo.setDireccion(dato.getDireccion());
            personanuevo.setTelefono(dato.getTelefono());
            personanuevo.setCelular(dato.getCelular());
            personanuevo.setCorreo(dato.getCorreo());
            personanuevo.setEstado(true);
            iPersonasAod.adicionar(personanuevo);
            usuarionuevo.setUsuario(dato.getDip());
            usuarionuevo.setClave(dato.getDip());
            usuarionuevo.setEstado(true);
            usuarionuevo.setIdpersona(personanuevo.getIdpersona());
            iUsuariosAod.adicionar(usuarionuevo);
            usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
            usuariorolnuevo.setIdrol(2L);
            iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
            representantenuevo.setIdpersona(personanuevo.getIdpersona());
            iRepresentantesAod.adicionar(representantenuevo);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }


}