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

import bo.sddpi.reactivatic.modulos.aods.IAtributosAod;
import bo.sddpi.reactivatic.modulos.aods.ICarritosAod;
import bo.sddpi.reactivatic.modulos.aods.IClientesAod;
import bo.sddpi.reactivatic.modulos.aods.IColoresAod;
import bo.sddpi.reactivatic.modulos.aods.IMaterialesAod;
import bo.sddpi.reactivatic.modulos.aods.IMunicipiosAod;
import bo.sddpi.reactivatic.modulos.aods.IPersonasAod;
import bo.sddpi.reactivatic.modulos.aods.IPreciosAod;
import bo.sddpi.reactivatic.modulos.aods.IProductosAod;
import bo.sddpi.reactivatic.modulos.aods.IRubrosAod;
import bo.sddpi.reactivatic.modulos.aods.ISolicitudesAod;
import bo.sddpi.reactivatic.modulos.aods.ITamanosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosrolesAod;
import bo.sddpi.reactivatic.modulos.entidades.Atributos;
import bo.sddpi.reactivatic.modulos.entidades.Carritos;
import bo.sddpi.reactivatic.modulos.entidades.Clientes;
import bo.sddpi.reactivatic.modulos.entidades.Colores;
import bo.sddpi.reactivatic.modulos.entidades.Correos;
import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.entidades.Materiales;
import bo.sddpi.reactivatic.modulos.entidades.Municipios;
import bo.sddpi.reactivatic.modulos.entidades.Personas;
import bo.sddpi.reactivatic.modulos.entidades.Precios;
import bo.sddpi.reactivatic.modulos.entidades.Procesar;
import bo.sddpi.reactivatic.modulos.entidades.Productos;
import bo.sddpi.reactivatic.modulos.entidades.Rubros;
import bo.sddpi.reactivatic.modulos.entidades.Tamanos;
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
    IPreciosAod iPreciosAod;

    @Autowired
    IColoresAod iColoresAod;

    @Autowired
    IMaterialesAod iMaterialesAod;

    @Autowired
    ITamanosAod iTamanosAod;

    @Autowired
    IAtributosAod iAtributosAod;

    @Autowired
    IMunicipiosAod iMunicipiosAod;

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
            @RequestParam(value = "cantidad", defaultValue = "12") Integer cantidad,
            @RequestParam(value = "orden" , defaultValue = "asc") String orden) {
        List<Productos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina - 1) * cantidad < 0) {
                nropagina = 0;
            } else {
                nropagina = (pagina - 1) * cantidad;
            }
            datos = iProductosAod.datoscat(buscar, nropagina, cantidad, orden);
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

    @GetMapping("/imagen/download")
    public ResponseEntity<?> download(@RequestParam("id") Long id, @RequestParam("tipo") String tipo){
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, String>> imagenes = iSubirarchivosServ.downloadimagen(id, tipo);

            if (imagenes.isEmpty()) {
                response.put("mensaje", "No se encontraron imágenes.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(imagenes);
        } catch (RuntimeException e) {
            response.put("error", "Error al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Manejar otros errores no controlados
            response.put("error", "Error inesperado al obtener las imágenes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @GetMapping(value="/descargarimagenproducto/{idproducto}/{nombreimagen}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargarImagenCarrito(@PathVariable Long idproducto, @PathVariable String nombreimagen) {
        Map<String, Object> mensajes = new HashMap<>();
        Resource archivo = null;
        try {
            archivo = iSubirarchivosServ.descargarimageneproductocarrito(idproducto.toString(), nombreimagen);
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

    @GetMapping("/productos")
    ResponseEntity<?> municipioproductos(){
        List<Municipios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iMunicipiosAod.productomunicipio();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Municipios>>(datos, HttpStatus.OK);
    }

    @GetMapping("/precios/{id}")
    ResponseEntity<?> precios(@PathVariable Long id){
        List<Precios> precios = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            precios = iPreciosAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Precios>>(precios, HttpStatus.OK);
    }

    @GetMapping("/colores/{id}")
    ResponseEntity<?> colores(@PathVariable Long id){
        List<Colores> colores = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            colores = iColoresAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Colores>>(colores, HttpStatus.OK);
    }

    @GetMapping("/materiales/{id}")
    ResponseEntity<?> materiales(@PathVariable Long id){
        List<Materiales> materiales = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            materiales = iMaterialesAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Materiales>>(materiales, HttpStatus.OK);
    }

    @GetMapping("/tamanos/{id}")
    ResponseEntity<?> tamanos(@PathVariable Long id){
        List<Tamanos> tamanos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tamanos = iTamanosAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Tamanos>>(tamanos, HttpStatus.OK);
    }

    @GetMapping("/atributos/{id}")
    ResponseEntity<?> atributos(@PathVariable Long id){
        List<Atributos> atributos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            atributos = iAtributosAod.lista(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Atributos>>(atributos, HttpStatus.OK);
    }

    // @PostMapping
    // ResponseEntity<?> fprocesar(@RequestBody Procesar dato) {
    //     Map<String, Object> mensajes = new HashMap<>();
    //     try {
    //         Long idusuario = iUsuariosAod.verificausuarioregistro(dato.getCelular().toString());
    //         if (idusuario == null) {
    //             Usuarios usuarionuevo = new Usuarios();
    //             Personas personanuevo = new Personas();
    //             Clientes clientenuevo = new Clientes();
    //             personanuevo.setPrimerapellido(dato.getNombre());
    //             personanuevo.setCelular(dato.getCelular().toString());
    //             personanuevo.setCorreo(dato.getCorreo());
    //             iPersonasAod.adicionar(personanuevo);
    //             usuarionuevo.setUsuario(dato.getCelular().toString());
    //             usuarionuevo.setClave(dato.getCelular().toString());
    //             usuarionuevo.setEstado(true);
    //             usuarionuevo.setIdpersona(personanuevo.getIdpersona());
    //             iUsuariosAod.adicionar(usuarionuevo);
    //             Usuariosroles usuariorolnuevo = new Usuariosroles();
    //             usuariorolnuevo.setIdusuario(usuarionuevo.getIdusuario());
    //             usuariorolnuevo.setIdrol(7L);
    //             iUsuariosrolesAod.adicionarusuariorol(usuariorolnuevo);
    //             clientenuevo.setIdpersona(personanuevo.getIdpersona());
    //             iClientesAod.adicionar(clientenuevo);
    //             iSolicitudesAod.procesasolicitud(dato.getIdcliente(), usuarionuevo.getIdusuario());
    //             List<Carritos> datoscat = null;
    //             try {
    //                 List<Empresas> enviar = iSolicitudesAod.buscarporempresas(dato.getIdcliente());
    //                 for (Empresas empresa : enviar) {
    //                     if (empresa.getCorreo() != null) {
    //                         try {
    //                             Context context = new Context();
    //                             context.setVariable("nombre", dato.getNombre());
    //                             context.setVariable("empresa", empresa.getEmpresa());
    //                             context.setVariable("logo", "logo");
    //                             String contenido = plantilla.process("correoempresa", context);
    //                             Correos correo = new Correos();
    //                             correo.setCorreo(empresa.getCorreo());
    //                             correo.setTema("Solicitud de productos");
    //                             correo.setContenido(contenido);
    //                             iCorreosServ.enviarcorreo(correo);
    //                         } catch (Exception e) {
    //                             System.out.println("Error: "+e.toString());
    //                         }
    //                     }
    //                 }
    //             } catch (Exception e) {
    //                 System.out.println("Error: "+e.toString());
    //             }
    //             if (dato.getCorreo() != null) {
    //                 try {
    //                     datoscat = iCarritosAod.datosl(dato.getIdcliente());
    //                     Context context = new Context();
    //                     context.setVariable("nombre", dato.getNombre());
    //                     context.setVariable("logo", "logo");
    //                     context.setVariable("datoscat", datoscat);
    //                     String contenido = plantilla.process("correocliente", context);
    //                     Correos correo = new Correos();
    //                     correo.setCorreo(dato.getCorreo());
    //                     correo.setTema("Solicitud de productos");
    //                     correo.setContenido(contenido);
    //                     iCorreosServ.enviarcorreo(correo);
    //                 } catch (Exception e) {
    //                     System.out.println("Error: "+e.toString());
    //                 }
    //             }
    //         } else {
    //             iSolicitudesAod.procesasolicitud(dato.getIdcliente(), idusuario);
    //             List<Carritos> datoscat = null;
    //             try {
    //                 List<Empresas> enviar = iSolicitudesAod.buscarporempresas(dato.getIdcliente());
    //                 for (Empresas empresa : enviar) {
    //                     if (empresa.getCorreo() !=null) {
    //                         try {
    //                             Context context = new Context();
    //                             context.setVariable("nombre", dato.getNombre());
    //                             context.setVariable("empresa", empresa.getEmpresa());
    //                             context.setVariable("logo", "logo");
    //                             String contenido = plantilla.process("correoempresa", context);
    //                             Correos correo = new Correos();
    //                             correo.setCorreo(empresa.getCorreo());
    //                             correo.setTema("Solicitud de productos");
    //                             correo.setContenido(contenido);
    //                             iCorreosServ.enviarcorreo(correo);
    //                         } catch (Exception e) {
    //                             System.out.println("Error: "+e.toString());
    //                         }
    //                     }
    //                 }
    //             } catch (Exception e) {
    //                 System.out.println("Error: "+e.toString());
    //             }
    //             if (dato.getCorreo() != null) {
    //                 try {
    //                     datoscat = iCarritosAod.datosl(dato.getIdcliente());
    //                     Context context = new Context();
    //                     context.setVariable("nombre", dato.getNombre());
    //                     context.setVariable("logo", "logo");
    //                     context.setVariable("datoscat", datoscat);
    //                     String contenido = plantilla.process("correocliente", context);
    //                     Correos correo = new Correos();
    //                     correo.setCorreo(dato.getCorreo());
    //                     correo.setTema("Solicitud de productos");
    //                     correo.setContenido(contenido);
    //                     iCorreosServ.enviarcorreo(correo);
    //                 } catch (Exception e) {
    //                     System.out.println("Error: "+e.toString());
    //                 }
    //             }
    //         }
    //     } catch (DataAccessException e) {
    //         mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
    //         mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
    //         return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    //     mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
    //     return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    // }

    @PostMapping("/usuariocatalogo")
    ResponseEntity<?> usuariocatalogo(@RequestBody Procesar dato) {
        Map<String, Object> mensajes = new HashMap<>();
        Procesar datom = null;
        try {
            // Intenta obtener los datos del usuario
            datom = iUsuariosAod.usuariocatalogo(dato);
            
            if (datom == null) {
                // Usuario no encontrado
                mensajes.put("mensaje", "Usuario no encontrado.");
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
            }
            
            // Verifica la contraseña
            if (!BCrypt.checkpw(dato.getClave(), datom.getClave())) {
                // Contraseña incorrecta
                mensajes.put("mensaje", "Usuario o contraseña incorrectos.");
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.UNAUTHORIZED);
            }
            
        } catch (DataAccessException e) {
            // Error al acceder a la base de datos
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos.");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Manejo de otros errores
            mensajes.put("mensaje", "Ocurrió un error inesperado.");
            mensajes.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        // Retorna el objeto Procesar si la autenticación es exitosa
        return new ResponseEntity<Procesar>(datom, HttpStatus.OK);
    }
}
