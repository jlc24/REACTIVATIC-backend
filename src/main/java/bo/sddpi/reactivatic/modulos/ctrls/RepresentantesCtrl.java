package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.IRepresentantesAod;
import bo.sddpi.reactivatic.modulos.aods.IUsuariosAod;
import bo.sddpi.reactivatic.modulos.entidades.Representantes;
import bo.sddpi.reactivatic.modulos.entidades.Usuarios;
import bo.sddpi.reactivatic.modulos.reportes.IRepresentantesRep;

@RestController
@RequestMapping("/apirest/representantes")
public class RepresentantesCtrl {

    @Autowired
    IUsuariosAod iUsuariosAod;

    @Autowired
    IRepresentantesRep iRepresentantesRep;

    @Autowired
    IRepresentantesAod iRepresentantesAod;

    @GetMapping("/buscar")
    ResponseEntity<?> search(@RequestParam(value = "buscar", defaultValue = "") String buscar){
        List<Representantes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iRepresentantesAod.buscar(buscar);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Representantes>>(datos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    ResponseEntity<?> dato(@PathVariable Long id){
        Representantes representante = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            representante = iRepresentantesAod.dato(id);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (representante == null) {
            mensajes.put("mensaje", "El id: ".concat(id.toString()).concat(" no existe en la Base de Datos"));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Representantes>(representante, HttpStatus.OK);
    }
    

    @GetMapping("/l")
    ResponseEntity<?> datosl() {
        List<Representantes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iRepresentantesAod.datosl();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Representantes>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/datosXLS", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<?> datosXLS(@RequestParam(value = "buscar", defaultValue = "") String buscar) {
        byte[] data = null;
        List<Usuarios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iUsuariosAod.datosreporep(buscar);
            data = iRepresentantesRep.datosXLS(datos);
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
            datos = iUsuariosAod.datosreporep(buscar);
            data = iRepresentantesRep.datosPDF(datos);
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar el archivo Excel");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<byte[]>(data, HttpStatus.OK);
    }

}
