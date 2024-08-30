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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import bo.sddpi.reactivatic.modulos.aods.IEmpresasAod;
import bo.sddpi.reactivatic.modulos.aods.IProductosAod;
import bo.sddpi.reactivatic.modulos.entidades.Productos;
import bo.sddpi.reactivatic.modulos.servicios.ISubirarchivosServ;

@RestController
@RequestMapping("/apirest/productos")
public class ProductosCtrl {

    @Autowired
    IProductosAod iProductosAod;

    @Autowired
    IEmpresasAod iEmpresasAod;

    @Autowired
    ISubirarchivosServ iSubirarchivosServ;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Productos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina - 1) * cantidad < 0) {
                nropagina = 0;
            } else {
                nropagina = (pagina - 1) * cantidad;
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idempresa = iEmpresasAod.idempresa(Long.parseLong(auth.getName()));
            datos = iProductosAod.datos(idempresa, buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Productos>>(datos, HttpStatus.OK);
    }

    @GetMapping("/admin")
    ResponseEntity<?> datosAdmin(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
                                @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad,
                                @RequestParam(value = "rubro", defaultValue = "") String rubro) {
        List<Productos> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina - 1) * cantidad < 0) {
                nropagina = 0;
            } else {
                nropagina = (pagina - 1) * cantidad;
            }
            datos = iProductosAod.datosAdmin(buscar, nropagina, cantidad, rubro);
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
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idempresa = iEmpresasAod.idempresa(Long.parseLong(auth.getName()));
            cantidad = iProductosAod.cantidad(idempresa, buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/cantidad")
    ResponseEntity<?> cantidadAdmin(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                                    @RequestParam(value = "rubro", defaultValue = "") String rubro) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iProductosAod.cantidadAdmin(buscar, rubro);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }
    
    @GetMapping(value = "/cantidadtotal")
    ResponseEntity<?> cantidadtotal(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // Long idempresa = iEmpresasAod.idempresa(Long.parseLong(auth.getName()));
            cantidad = iProductosAod.cantidadTotal(buscar);
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
            dato = iProductosAod.dato(id);
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

    @PostMapping
    ResponseEntity<?> adicionar(@Valid @RequestBody Productos dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idempresa = iEmpresasAod.idempresa(Long.parseLong(auth.getName()));
            dato.setIdempresa(idempresa);
            iProductosAod.adicionar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Productos>(dato, HttpStatus.OK);
    }

    @PutMapping
    ResponseEntity<?> modificar(@Valid @RequestBody Productos dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        if (resultado.hasErrors()) {
            List<String> errores = resultado.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());
            mensajes.put("errores", errores);
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Long idempresa = iEmpresasAod.idempresa(Long.parseLong(auth.getName()));
            dato.setIdempresa(idempresa);
            iProductosAod.modificar(dato);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PostMapping(value = "/cargarp/{id}")
    ResponseEntity<?> cargar(@RequestParam("archivo") MultipartFile archivo, @PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iSubirarchivosServ.cargarimagenproducto(id, archivo);
            iSubirarchivosServ.redimensionarproducto(id);
            iSubirarchivosServ.fusionarproducto(id);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo cargar el archivo: " + archivo.getOriginalFilename() + "!!! error:" + e.toString());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se cargo el archivo con éxito: " + archivo.getOriginalFilename());
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminarp/{id}")
    ResponseEntity<?> cargar(@PathVariable Long id) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iSubirarchivosServ.eliminarimagenproducto(id);
        } catch (Exception e) {
            mensajes.put("mensaje", "No se pudo eliminar el archivo: !!! error:" + e.toString());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se elimino el archivo con éxito: ");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping(value="/descargarproducto/{idproducto}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    ResponseEntity<?> descargas (@PathVariable Long idproducto) {
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

}
