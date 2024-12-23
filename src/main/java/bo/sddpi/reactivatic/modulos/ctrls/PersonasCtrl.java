package bo.sddpi.reactivatic.modulos.ctrls;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;

import bo.sddpi.reactivatic.modulos.aods.IClientesAod;
import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Clientes;
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
    IClientesAod iClientesAod;

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

    @GetMapping(value = "/ver/{id}")
    ResponseEntity<?> persona(@PathVariable Long id) {
        Personas dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iPersonasAod.persona(id);
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
        Long verificarpersona = iPersonasAod.verificarpersonaregistro(dato.getPrimerapellido(), dato.getSegundoapellido(), dato.getPrimernombre(), dato.getDip());
        Long verificarusuario = iUsuariosAod.verificausuarioregistro(dato.getUsuario().getUsuario());
        if (verificarpersona != null) {
            mensajes.put("mensaje", "La persona ya se encuentra registrada en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else if (verificarusuario != null) {
            mensajes.put("mensaje", "El usuario ya existe.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                Usuarios usuarionuevo = new Usuarios();
                Personas personanuevo = new Personas();
                Usuariosroles usuariorolnuevo = new Usuariosroles();
                Clientes clientenuevo = new Clientes();
                
                personanuevo.setIdtipogenero(dato.getIdtipogenero());
                personanuevo.setPrimerapellido(dato.getPrimerapellido());
                personanuevo.setSegundoapellido(dato.getSegundoapellido());
                personanuevo.setPrimernombre(dato.getPrimernombre());
                personanuevo.setDip(dato.getDip());
                personanuevo.setComplementario(dato.getComplementario());
                personanuevo.setIdtipodocumento(dato.getIdtipodocumento());
                personanuevo.setIdtipoextension(dato.getIdtipoextension());
                personanuevo.setDireccion(dato.getDireccion());
                personanuevo.setTelefono(dato.getTelefono());
                personanuevo.setCelular(dato.getCelular());
                personanuevo.setCorreo(dato.getCorreo());
                personanuevo.setFormacion(dato.getFormacion());
                personanuevo.setEstadocivil(dato.getEstadocivil());
                personanuevo.setHijos(dato.getHijos());
                personanuevo.setEstado(true);
                iPersonasAod.adicionar(personanuevo);
                
                usuarionuevo.setUsuario(dato.getUsuario().getUsuario());
                usuarionuevo.setClave(dato.getUsuario().getClave());
                usuarionuevo.setEstado(true);
                usuarionuevo.setIdpersona(personanuevo.getIdpersona());
                usuarionuevo.setIdcargo(dato.getUsuario().getIdcargo());
                iUsuariosAod.adicionar(usuarionuevo);
                
                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(dato.getRol().getIdrol());
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);

                clientenuevo.setIdpersona(personanuevo.getIdpersona());
                iClientesAod.adicionar(clientenuevo);

                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(8L);
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        }
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
            iUsuariosAod.modificar(dato.getUsuario());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping(value = "/cambiarestado")
    ResponseEntity<?> cambiarestado(@RequestBody Personas persona) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iPersonasAod.cambiarestado(persona);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado el estado del usuario");
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

    @PostMapping(value="/uploadperfil")
    public ResponseEntity<Map<String, Object>> uploadPerfil(@RequestParam("tipo") String tipo, @RequestParam("archivo") MultipartFile archivo) {
        Map<String, Object> mensajes = new HashMap<>();

        if (archivo.isEmpty()) {
            mensajes.put("mensaje", "No se ha seleccionado ningún archivo.");
            return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long id = Long.parseLong(auth.getName());
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
    public ResponseEntity<?> downloadImage(@RequestParam("tipo") String tipo){
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long id;
            if (tipo.equals("repanverso") || tipo.equals("repreverso")) {
                id = iPersonasAod.verificarpersona(Long.parseLong(auth.getName()));
            }else{
                id = Long.parseLong(auth.getName());
            }

            List<Map<String, String>> imagenes = iSubirarchivosServ.downloadimagen(id, tipo);

            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            response.put("error", "Error al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @GetMapping("/download")
    public ResponseEntity<?> download(@RequestParam("id") Long id, @RequestParam("tipo") String tipo){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, String>> imagenes = iSubirarchivosServ.downloadimagen(id, tipo);

            return ResponseEntity.ok(imagenes);
        } catch (Exception e) {
            response.put("error", "Error al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
        Long verificarpersona = iPersonasAod.verificarpersonaregistro(dato.getPrimerapellido(), dato.getSegundoapellido(), dato.getPrimernombre(), dato.getDip());
        Long verificarusuario = iUsuariosAod.verificausuarioregistro(dato.getUsuario().getUsuario());
        if (verificarpersona != null) {
            mensajes.put("mensaje", "La persona ya se encuentra registrada en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else if (verificarusuario != null) {
            mensajes.put("mensaje", "El usuario ya existe.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                Usuarios usuarionuevo = new Usuarios();
                Personas personanuevo = new Personas();
                Representantes representantenuevo = new Representantes();
                Usuariosroles usuariorolnuevo = new Usuariosroles();
                Clientes clientenuevo = new Clientes();

                personanuevo.setIdtipogenero(dato.getIdtipogenero());
                personanuevo.setPrimerapellido(dato.getPrimerapellido());
                personanuevo.setSegundoapellido(dato.getSegundoapellido());
                personanuevo.setPrimernombre(dato.getPrimernombre());
                personanuevo.setDip(dato.getDip());
                personanuevo.setComplementario(dato.getComplementario());
                personanuevo.setIdtipodocumento(dato.getIdtipodocumento());
                personanuevo.setIdtipoextension(dato.getIdtipoextension());
                personanuevo.setDireccion(dato.getDireccion());
                personanuevo.setTelefono(dato.getTelefono());
                personanuevo.setCelular(dato.getCelular());
                personanuevo.setCorreo(dato.getCorreo());
                personanuevo.setFormacion(dato.getFormacion());
                personanuevo.setEstadocivil(dato.getEstadocivil());
                personanuevo.setHijos(dato.getHijos());
                personanuevo.setEstado(true);
                iPersonasAod.adicionar(personanuevo);

                usuarionuevo.setUsuario(dato.getUsuario().getUsuario());
                usuarionuevo.setClave(dato.getUsuario().getClave());
                usuarionuevo.setEstado(true);
                usuarionuevo.setIdpersona(personanuevo.getIdpersona());
                iUsuariosAod.adicionar(usuarionuevo);

                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(7L);
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);

                representantenuevo.setIdpersona(personanuevo.getIdpersona());
                iRepresentantesAod.adicionar(representantenuevo);

                clientenuevo.setIdpersona(personanuevo.getIdpersona());
                iClientesAod.adicionar(clientenuevo);

                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(8L);
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);

            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @PostMapping("/adicionarcli")
    ResponseEntity<?> adicionarCli(@Valid @RequestBody Personas dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream().map(err -> "El campo '"+err.getField()+"' "+err.getDefaultMessage()).collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        Long verificarpersona = iPersonasAod.verificarpersonaregistro(dato.getPrimerapellido(), dato.getSegundoapellido(), dato.getPrimernombre(), dato.getDip());
        Long verificarusuario = iUsuariosAod.verificausuarioregistro(dato.getUsuario().getUsuario());
        if (verificarpersona != null) {
            mensajes.put("mensaje", "La persona ya se encuentra registrada en el sistema.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else if (verificarusuario != null) {
            mensajes.put("mensaje", "El usuario ya existe.");
            return new ResponseEntity<>(mensajes, HttpStatus.CONFLICT);
        }else{
            try {
                Usuarios usuarionuevo = new Usuarios();
                Personas personanuevo = new Personas();
                Usuariosroles usuariorolnuevo = new Usuariosroles();
                Clientes clientenuevo = new Clientes();

                personanuevo.setPrimerapellido(dato.getPrimerapellido());
                personanuevo.setSegundoapellido(dato.getSegundoapellido());
                personanuevo.setPrimernombre(dato.getPrimernombre());
                personanuevo.setDireccion(dato.getDireccion());
                personanuevo.setCelular(dato.getCelular());
                personanuevo.setCorreo(dato.getCorreo());
                personanuevo.setEstado(true);
                iPersonasAod.adicionar(personanuevo);

                usuarionuevo.setUsuario(dato.getUsuario().getUsuario());
                usuarionuevo.setClave(dato.getUsuario().getClave());
                usuarionuevo.setEstado(true);
                usuarionuevo.setIdpersona(personanuevo.getIdpersona());
                iUsuariosAod.adicionar(usuarionuevo);

                clientenuevo.setIdpersona(personanuevo.getIdpersona());
                iClientesAod.adicionar(clientenuevo);

                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(8L);
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);

            } catch (DataAccessException e) {
                mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
                mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
        }
    }

    @GetMapping("/roles/{id}")
    ResponseEntity<?> usuariosrol(@PathVariable Long id){
        List<Personas> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iPersonasAod.obtenerPersonasPorRol(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Personas>>(datos, HttpStatus.OK);
    }

    @PostMapping("/generarusuario")
    ResponseEntity<?> generarusuario(@RequestBody Map<String, Long> payload){
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Long id = payload.get("id");
            if (id == null) {
                mensajes.put("mensaje", "El ID es requerido");
                return new ResponseEntity<>(mensajes, HttpStatus.BAD_REQUEST);
            }

            Personas persona = iPersonasAod.dato(id);
            if (persona == null) {
                mensajes.put("mensaje", "No se encontró la persona con el ID proporcionado");
                return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND);
            }

            String primerApellido = persona.getPrimerapellido() != null ? persona.getPrimerapellido() : "";
            String segundoApellido = persona.getSegundoapellido() != null ? persona.getSegundoapellido() : "";
            String primerNombre = persona.getPrimernombre() != null ? persona.getPrimernombre() : "";
            String dip = persona.getDip() != null ? persona.getDip() : "";

            String parteUsuario;
            if (segundoApellido.isEmpty()) {
                parteUsuario = primerApellido.substring(0, Math.min(4, primerApellido.length())) + 
                            primerNombre.substring(0, Math.min(2, primerNombre.length()));
            } else {
                parteUsuario = primerApellido.substring(0, Math.min(2, primerApellido.length())) +
                            segundoApellido.substring(0, Math.min(2, segundoApellido.length())) +
                            primerNombre.substring(0, Math.min(2, primerNombre.length()));
            }

            String parteDIP = dip.length() >= 3 ? dip.substring(dip.length() - 3) : dip;
            String usuarioGenerado = parteUsuario.toLowerCase() + parteDIP;
            String claveGenerada = usuarioGenerado; 

            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setIdpersona(persona.getIdpersona());
            nuevoUsuario.setUsuario(usuarioGenerado);
            nuevoUsuario.setClave(claveGenerada);
            iUsuariosAod.adicionar(nuevoUsuario);

            Long idUsuarioGenerado = nuevoUsuario.getIdusuario();
            Usuariosroles usuarioRol = new Usuariosroles();
            usuarioRol.setIdusuario(idUsuarioGenerado);
            usuarioRol.setIdrol(7L); 
            iUsuariosrolesAod.adicionarusuariorol(usuarioRol);

            Usuariosroles usuarioRolCliente = new Usuariosroles();
            usuarioRolCliente.setIdusuario(idUsuarioGenerado);
            usuarioRolCliente.setIdrol(8L); 
            iUsuariosrolesAod.adicionarusuariorol(usuarioRolCliente);

        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la operación en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Usuario generado correctamente");
        return new ResponseEntity<>(mensajes, HttpStatus.CREATED);
    }
    
    @GetMapping("/carnet/{idpersona}")
    public ResponseEntity<?> generarCarnetPdf(@PathVariable Long idpersona) {
        try {
            // Llamamos al servicio que genera el PDF
            byte[] pdfBytes = iSubirarchivosServ.generarCarnetPdf(idpersona);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=carnet_" + idpersona + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (IOException e) {
            Map<String, Object> mensajes = new HashMap<>();
            mensajes.put("mensaje", "Error: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.NOT_FOUND); // Cambiar a NOT_FOUND ya que el recurso no existe.

        } catch (DocumentException e) {
            Map<String, Object> mensajes = new HashMap<>();
            mensajes.put("mensaje", "Error al generar el PDF: " + e.getMessage());
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}