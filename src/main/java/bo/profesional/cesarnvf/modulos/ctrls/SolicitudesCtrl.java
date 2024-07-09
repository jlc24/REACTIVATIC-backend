package bo.profesional.cesarnvf.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.ISolicitudesAod;
import bo.profesional.cesarnvf.modulos.entidades.Solicitudes;
import bo.profesional.cesarnvf.modulos.reportes.ISolicitudesRep;
import bo.profesional.cesarnvf.modulos.servicios.ICorreosServ;

@RestController
@RequestMapping("/apirest/solicitudes")
public class SolicitudesCtrl {

    @Autowired
    ISolicitudesAod iSolicitudesAod;

    @Autowired
    ISolicitudesRep iSolicitudesRep;

    @Autowired
    ICorreosServ iCorreosServ;

    @GetMapping
    ResponseEntity<?> datos(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Solicitudes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            datos = iSolicitudesAod.datos(Long.parseLong(auth.getName()), nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Solicitudes>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad() {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            cantidad = iSolicitudesAod.cantidad(Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/datose")
    ResponseEntity<?> datose(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Solicitudes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            datos = iSolicitudesAod.datose(Long.parseLong(auth.getName()), nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Solicitudes>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidade")
    ResponseEntity<?> cantidade() {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            cantidad = iSolicitudesAod.cantidade(Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/procesasolicitud")
    ResponseEntity<?> procesasolicitud(@RequestParam(value = "idclientecarrito", defaultValue = "") Long idclientecarrito) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            int resultado = iSolicitudesAod.procesasolicitud(idclientecarrito, Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha procesado la solicitud correctamente en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @PutMapping("/actualizarestado")
    ResponseEntity<?> modificar(@RequestBody Solicitudes dato, BindingResult resultado) {
        Map<String, Object> mensajes = new HashMap<>();
        try {
            iSolicitudesAod.actualizarestado(dato.getIdsolicitud());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        mensajes.put("mensaje", "Se ha modificado correctamente el dato en la Base de Datos");
        return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.OK);
    }

    @GetMapping("/datosrep")
    ResponseEntity<?> datosr(@RequestParam(value = "buscar", defaultValue = "") String buscar,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "cantidad", defaultValue = "10") Integer cantidad) {
        List<Solicitudes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        int nropagina = 0;
        try {
            if ((pagina-1)*cantidad<0) {
                nropagina = 0;
            } else {
                nropagina = (pagina-1)*cantidad;
            }
            datos = iSolicitudesAod.datosr(buscar, nropagina, cantidad);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Solicitudes>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidadrep")
    ResponseEntity<?> cantidadr(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = iSolicitudesAod.cantidadr(buscar);
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
        List<Solicitudes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iSolicitudesAod.datosrepo(buscar);
            data = iSolicitudesRep.datosXLS(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/datosPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    ResponseEntity<?> datosPDF(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Solicitudes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iSolicitudesAod.datosrepo(buscar);
            data = iSolicitudesRep.datosPDF(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

}
