package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
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
import org.springframework.web.bind.annotation.*;

import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.reportes.IUsuariosRep;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    private ResponseEntity<?> obtenerDatos(Callable<List<Usuarios>> consultaDatos) {
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = consultaDatos.call();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al ejecutar la consulta");
            mensajes.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Usuarios>>(datos, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        int nropagina = Math.max((pagina - 1) * cantidad, 0);
        return obtenerDatos(() -> iUsuariosAod.datos(buscar, nropagina, cantidad));
    }
    @GetMapping("/sddpi")
    public ResponseEntity<?> datossddpi(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                                @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        int nropagina = Math.max((pagina - 1) * cantidad, 0);
        return obtenerDatos(() -> iUsuariosAod.datossddpi(buscar, nropagina, cantidad));
    }
    @GetMapping("/dpeic")
    public ResponseEntity<?> datosdpeic(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                                @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        int nropagina = Math.max((pagina - 1) * cantidad, 0);
        return obtenerDatos(() -> iUsuariosAod.datosdpeic(buscar, nropagina, cantidad));
    }
    @GetMapping("/reactivatic")
    public ResponseEntity<?> datosreactivatic(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                        @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                                        @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        int nropagina = Math.max((pagina - 1) * cantidad, 0);
        return obtenerDatos(() -> iUsuariosAod.datosreactivatic(buscar, nropagina, cantidad));
    }

    private ResponseEntity<?> obtenerCantidad(Callable<Integer> consultaCantidad) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = consultaCantidad.call();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al ejecutar la consulta");
            mensajes.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    public ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        return obtenerCantidad(() -> iUsuariosAod.cantidad(buscar));
    }

    @GetMapping(value = "/cantidad/sddpi")
    public ResponseEntity<?> cantidadsddpi(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        return obtenerCantidad(() -> iUsuariosAod.cantidadsddpi(buscar));
    }
    @GetMapping(value = "/cantidad/dpeic")
    public ResponseEntity<?> cantidaddpeic(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        return obtenerCantidad(() -> iUsuariosAod.cantidaddpeic(buscar));
    }
    @GetMapping(value = "/cantidad/reactivatic")
    public ResponseEntity<?> cantidadreactivatic(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        return obtenerCantidad(() -> iUsuariosAod.cantidadreactivatic(buscar));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> dato(@PathVariable Long id) {
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

    @GetMapping(value = "/representante/{id}")
    public ResponseEntity<?> datorep(@PathVariable Long id) {
        Usuarios dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iUsuariosAod.datorep(id);
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

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Usuarios dato, BindingResult resultado){
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarusuario = iUsuariosAod.verificausuarioregistro(dato.getUsuario());
        if (verificarusuario != null) {
            mensajes.put("mensaje", "El usuario ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                iUsuariosAod.adicionar(dato);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Usuarios dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarusuario = iUsuariosAod.verificausuarioregistro(dato.getUsuario());
        if (verificarusuario != null) {
            mensajes.put("mensaje", "El usuario ya se encuentra registrado en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
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
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<?> eliminar(@PathVariable Long id) {
        // Map<String, Object> mensajes = new HashMap<>();
        // try {
        //     Personas persona = iPersonasAod.infoadicional(id);
        //     iUsuariosrolesAod.eliminar(id);
        //     iUsuariosAod.eliminar(id);
        //     iPersonasAod.eliminar(persona.getIdpersona());
        // } catch (DataAccessException e) {
        //     mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
        //     mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
        //     return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        // }
        // mensajes.put("mensaje", "Se ha borrado correctamente el dato en la Base de Datos");
        // return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        Map<String, Object> mensajes = new HashMap<>();
    try {
        // Verificar si la persona existe
        Optional<Personas> personaOpt = Optional.ofNullable(iPersonasAod.infoadicional(id));
        if (!personaOpt.isPresent()) {
            mensajes.put("mensaje", "El registro con el ID " + id + " no existe en la Base de Datos.");
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
        }

        // Eliminar en el orden adecuado
        iUsuariosrolesAod.eliminar(id);
        iUsuariosAod.eliminar(id);
        iPersonasAod.eliminar(personaOpt.get().getIdpersona());

        mensajes.put("mensaje", "El registro se ha eliminado correctamente.");
        return new ResponseEntity<>(mensajes, HttpStatus.OK);

    } catch (DataAccessException e) {
        mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos.");
        mensajes.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
        return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);

    } catch (Exception e) {
        mensajes.put("mensaje", "Error inesperado al intentar eliminar el registro.");
        mensajes.put("error", e.getMessage());
        return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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

    @PostMapping("/verificar")
    ResponseEntity<?> verificar(@RequestBody Usuarios usuario) {
        Boolean esClaveCorrecta;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idusuario = Long.parseLong(auth.getName());
            String user = iUsuariosAod.verificaruser(idusuario);
            usuario.setIdusuario(idusuario);
            usuario.setUsuario(user);
            esClaveCorrecta = iUsuariosAod.verificaclave(usuario);
            if (!esClaveCorrecta) {
                mensajes.put("mensaje", "Clave incorrecta");
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.UNAUTHORIZED);
            }
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Clave verificada correctamente");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }
    
    @PutMapping(value = "/cambiarestado")
    ResponseEntity<?> cambiarestado(@RequestBody Usuarios usuario) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iUsuariosAod.cambiarestado(usuario);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado el estado del usuario");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/datosrep")
    // ResponseEntity<?> datosrep(@RequestParam(value = "buscar", defaultValue = "") String buscar,
    //         @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
    //         @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
    //     List<Usuarios> datos = null;
    //     Map<String, Object> mensajes = new HashMap<>();
    //     int nropagina = 0;
    //     try {
    //         if ((pagina-1)*cantidad<0) {
    //             nropagina = 0;
    //         } else {
    //             nropagina = (pagina-1)*cantidad;
    //         }
    //         datos = iUsuariosAod.datosrep(buscar, nropagina, cantidad);
    //     } catch (DataAccessException e) {
    //         mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
    //         mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
    //         return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    //     return new ResponseEntity<List<Usuarios>>(datos, HttpStatus.OK);
    // }
    public ResponseEntity<?> datosrep(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        int nropagina = Math.max((pagina - 1) * cantidad, 0);
        return obtenerDatos(() -> iUsuariosAod.datosrep(buscar, nropagina, cantidad));
    }

    @GetMapping(value = "/cantidadrep")
    // ResponseEntity<?> cantidadrep(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
    //     Integer cantidad = null;
    //     Map<String, Object> mensajes = new HashMap<>();
    //     try {
    //         cantidad = iUsuariosAod.cantidadrep(buscar);
    //     } catch (DataAccessException e) {
    //         mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
    //         mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
    //         return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    //     return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    // }
    public ResponseEntity<?> cantidadrep(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        return obtenerCantidad(() -> iUsuariosAod.cantidadrep(buscar));
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