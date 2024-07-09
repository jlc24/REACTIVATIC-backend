package bo.profesional.cesarnvf.modulos.ctrls;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.IClientesAod;
import bo.profesional.cesarnvf.modulos.aods.IPersonasAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosrolesAod;
import bo.profesional.cesarnvf.modulos.entidades.Clientes;
import bo.profesional.cesarnvf.modulos.entidades.Personas;
import bo.profesional.cesarnvf.modulos.entidades.Registros;
import bo.profesional.cesarnvf.modulos.entidades.Usuarios;
import bo.profesional.cesarnvf.modulos.entidades.Usuariosroles;

@RestController
@RequestMapping("/WFXCYwnGZovBflrjQqlA/registros")
public class RegistrosCtrl {

    @Autowired
    IPersonasAod iPersonasAod;

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IUsuariosrolesAod iUsuariosrolesAod;

    @Autowired
    IClientesAod iClientesAod;

    @PostMapping
    ResponseEntity<?> registro(@Valid @RequestBody Registros dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verifica = iUsuariosAod.verificausuarioregistro(dato.getCelular());
        if (verifica!=null) {
            mensajes.put("mensaje", "Error el usuario ya esta registrado en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            Usuarios usuarionuevo = new Usuarios();
            Personas personanuevo = new Personas();
            Clientes clientenuevo = new Clientes();
            /* personanuevo.setPrimerapellido(dato.getPrimerapellido());
            personanuevo.setSegundoapellido(dato.getSegundoapellido()); */
            personanuevo.setPrimernombre(dato.getPrimernombre());
            /* personanuevo.setSegundonombre(dato.getSegundonombre());
            personanuevo.setDip(dato.getDip()); */
            personanuevo.setCelular(dato.getCelular());
            personanuevo.setCorreo(dato.getCorreo());
            iPersonasAod.adicionar(personanuevo);
            usuarionuevo.setUsuario(dato.getCelular());
            usuarionuevo.setClave(dato.getCelular());
            usuarionuevo.setEstado(true);
            usuarionuevo.setIdpersona(personanuevo.getIdpersona());
            iUsuariosAod.adicionar(usuarionuevo);
            Usuariosroles usuariorolnuevo = new Usuariosroles();
            usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
            usuariorolnuevo.setIdrol(3L);
            iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
            clientenuevo.setIdpersona(personanuevo.getIdpersona());
            iClientesAod.adicionar(clientenuevo);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha creado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.CREATED);
    }

}
