package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import bo.sddpi.reactivatic.modulos.aods.ICarritosAod;
import bo.sddpi.reactivatic.modulos.aods.IClientesAod;
import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IProductosAod;
import bo.sddpi.reactivatic.modulos.aods.IRubrosAod;
import bo.sddpi.reactivatic.modulos.aods.ISolicitudesAod;
import bo.sddpi.reactivatic.modulos.aods.ISubrubrosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Carritos;
import bo.sddpi.reactivatic.modulos.entidades.Clientes;
import bo.sddpi.reactivatic.modulos.entidades.Correos;
import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Procesar;
import bo.sddpi.reactivatic.modulos.entidades.Productos;
import bo.sddpi.reactivatic.modulos.entidades.Rubros;
import bo.sddpi.reactivatic.modulos.entidades.Subrubros;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.entidades.Usuariosroles;
import bo.sddpi.reactivatic.modulos.servicios.ICorreosServ;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@RestController
@RequestMapping("/catalogos")
public class CatalogosCtrl {

    @Autowired
    IProductosAod iProductosAod;

    @Autowired
    IRubrosAod iRubrosAod;

    @Autowired
    ISubrubrosAod iSubrubrosAod;

    @Autowired
    IPersonasAod iPersonasAod;

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IUsuariosrolesAod iUsuariosrolesAod;

    @Autowired
    IClientesAod iClientesAod;

    @Autowired
    ISolicitudesAod iSolicitudesAod;

    @Autowired
    ICorreosServ iCorreosServ;

    @Autowired
    private TemplateEngine plantilla;

    @Autowired
    ICarritosAod iCarritosAod;

    @Autowired
    ISubirarchivosServ iSubirarchivosServ;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "12") Integer cantidad) {
        List<Productos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina - 1) * cantidad < 0) {
                nropagina = 0;
            } else {
                nropagina = (pagina - 1) * cantidad;
            }
            datos = iProductosAod.datoscat(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Productos>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iProductosAod.cantidadcat(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<?> dato(@PathVariable Long id) {
        Productos dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = iProductosAod.datocat(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (dato == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Productos>(dato, HttpStatus.OK);
    }

    @GetMapping(value="/descargarproducto/{idproducto}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargas(@PathVariable Long idproducto) {
        Map<String, Object> mensajes = new HashMap<>();
        Resource archivo = null;
        try {
            archivo = iSubirarchivosServ.descargarimagenproducto(idproducto.toString());
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo descargar el archivo");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @GetMapping(value="/descargarempresa/{idempresa}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargasempresa(@PathVariable Long idempresa) {
        Map<String, Object> mensajes = new HashMap<>();
        Resource archivo = null;
        try {
            archivo = iSubirarchivosServ.descargarimagenempresa(idempresa.toString());
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo descargar el archivo");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"");
        return new ResponseEntity<Resource>(archivo, cabecera, HttpStatus.OK);
    }

    @GetMapping("/cantidadporrubros")
    ResponseEntity<?> cantidadporrubros() {
        List<Rubros> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iRubrosAod.cantidadporrubro();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Rubros>>(datos, HttpStatus.OK);
    }

    @GetMapping("/rubros")
    ResponseEntity<?> rubros(){
        List<Rubros> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iRubrosAod.datosl();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Rubros>>(datos, HttpStatus.OK);
    }

    @GetMapping("/subrubros/{id}")
    ResponseEntity<?> subrubros(@PathVariable Long id){
        List<Subrubros> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iSubrubrosAod.subrubros(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Subrubros>>(datos, HttpStatus.OK);
    }
    
    @GetMapping("/listasubrubros")
    ResponseEntity<?> listasubrubros(){
        List<Subrubros> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iSubrubrosAod.listaSubrubros();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Subrubros>>(datos, HttpStatus.OK);
    }
    

    @PostMapping
    ResponseEntity<?> fprocesar(@RequestBody Procesar dato) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Long idusuario = iUsuariosAod.verificausuarioregistro(dato.getCelular().toString());
            if (idusuario == null) {
                Usuarios usuarionuevo = new Usuarios();
                Personas personanuevo = new Personas();
                Clientes clientenuevo = new Clientes();
                personanuevo.setPrimerapellido(dato.getNombre());
                personanuevo.setCelular(dato.getCelular().toString());
                personanuevo.setCorreo(dato.getCorreo());
                iPersonasAod.adicionar(personanuevo);
                usuarionuevo.setUsuario(dato.getCelular().toString());
                usuarionuevo.setClave(dato.getCelular().toString());
                usuarionuevo.setEstado(true);
                usuarionuevo.setIdpersona(personanuevo.getIdpersona());
                iUsuariosAod.adicionar(usuarionuevo);
                Usuariosroles usuariorolnuevo = new Usuariosroles();
                usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
                usuariorolnuevo.setIdrol(3L);
                iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
                clientenuevo.setIdpersona(personanuevo.getIdpersona());
                iClientesAod.adicionar(clientenuevo);
                int resultado = iSolicitudesAod.procesasolicitud(dato.getIdcliente(), usuarionuevo.getIdusuario());
                List<Carritos> datoscat = null;
                try {
                    List<Empresas> enviar = iSolicitudesAod.buscarporempresas(dato.getIdcliente());
                    for (Empresas empresa : enviar) {
                        if (empresa.getCorreo() != null) {
                            try {
                                Context context = new Context();
                                context.setVariable("nombre", dato.getNombre());
                                context.setVariable("empresa", empresa.getEmpresa());
                                context.setVariable("logo", "logo");
                                String contenido = plantilla.process("correoempresa", context);
                                Correos correo = new Correos();
                                correo.setCorreo(empresa.getCorreo());
                                correo.setTema("Solicitud de productos");
                                correo.setContenido(contenido);
                                iCorreosServ.enviarcorreo(correo);
                            } catch (Exception e) {
                                System.out.println("Error: "+e.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: "+e.toString());
                }
                if (dato.getCorreo() != null) {
                    try {
                        datoscat = iCarritosAod.datosl(dato.getIdcliente());
                        Context context = new Context();
                        context.setVariable("nombre", dato.getNombre());
                        context.setVariable("logo", "logo");
                        context.setVariable("datoscat", datoscat);
                        String contenido = plantilla.process("correocliente", context);
                        Correos correo = new Correos();
                        correo.setCorreo(dato.getCorreo());
                        correo.setTema("Solicitud de productos");
                        correo.setContenido(contenido);
                        iCorreosServ.enviarcorreo(correo);
                    } catch (Exception e) {
                        System.out.println("Error: "+e.toString());
                    }
                }
            } else {
                int resultado = iSolicitudesAod.procesasolicitud(dato.getIdcliente(), idusuario);
                List<Carritos> datoscat = null;
                try {
                    List<Empresas> enviar = iSolicitudesAod.buscarporempresas(dato.getIdcliente());
                    for (Empresas empresa : enviar) {
                        if (empresa.getCorreo() !=null) {
                            try {
                                Context context = new Context();
                                context.setVariable("nombre", dato.getNombre());
                                context.setVariable("empresa", empresa.getEmpresa());
                                context.setVariable("logo", "logo");
                                String contenido = plantilla.process("correoempresa", context);
                                Correos correo = new Correos();
                                correo.setCorreo(empresa.getCorreo());
                                correo.setTema("Solicitud de productos");
                                correo.setContenido(contenido);
                                iCorreosServ.enviarcorreo(correo);
                            } catch (Exception e) {
                                System.out.println("Error: "+e.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error: "+e.toString());
                }
                if (dato.getCorreo() != null) {
                    try {
                        datoscat = iCarritosAod.datosl(dato.getIdcliente());
                        Context context = new Context();
                        context.setVariable("nombre", dato.getNombre());
                        context.setVariable("logo", "logo");
                        context.setVariable("datoscat", datoscat);
                        String contenido = plantilla.process("correocliente", context);
                        Correos correo = new Correos();
                        correo.setCorreo(dato.getCorreo());
                        correo.setTema("Solicitud de productos");
                        correo.setContenido(contenido);
                        iCorreosServ.enviarcorreo(correo);
                    } catch (Exception e) {
                        System.out.println("Error: "+e.toString());
                    }
                }
            }
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PostMapping("/usuariocatalogo")
    ResponseEntity<?> usuariocatalogo(@RequestBody Procesar dato) {
        Map<String, Object> mensajes = new HashMap<>();
        Procesar datom = null;
        try {
            datom = iUsuariosAod.usuariocatalogo(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (BCrypt.checkpw(dato.getClave(), datom.getClave())==false) {
            datom = null;
            return new ResponseEntity<Procesar>(datom, HttpStatus.OK);
        }
        return new ResponseEntity<Procesar>(datom, HttpStatus.OK);
    }


}
