package bo.profesional.cesarnvf.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.IReportesAod;
import bo.profesional.cesarnvf.modulos.entidades.Reportes;

@RestController
@RequestMapping("/apirest/reportes")
public class ReportesCtrl {

    @Autowired
    IReportesAod iReportesAod;

    @GetMapping("/empresasmassolicitadas")
    ResponseEntity<?> empresasmassolicitadas() {
        List<Reportes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iReportesAod.empresasmassolicitadas();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Reportes>>(datos, HttpStatus.OK);
    }

    @GetMapping("/clientesconmascompras")
    ResponseEntity<?> clientesconmascompras() {
        List<Reportes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iReportesAod.clientesconmascompras();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Reportes>>(datos, HttpStatus.OK);
    }

    @GetMapping("/productosmasvendidos")
    ResponseEntity<?> productosmasvendidos() {
        List<Reportes> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            datos = iReportesAod.productosmasvendidos();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Reportes>>(datos, HttpStatus.OK);
    }

}
