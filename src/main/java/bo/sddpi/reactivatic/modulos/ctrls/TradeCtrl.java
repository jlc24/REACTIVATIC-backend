package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.IBeneficiosAod;
import bo.sddpi.reactivatic.modulos.aods.INegociosAod;
import bo.sddpi.reactivatic.modulos.entidades.Beneficios;
import bo.sddpi.reactivatic.modulos.entidades.Negocios;

@RestController
@RequestMapping("/negocios")
public class TradeCtrl {

    @Autowired
    INegociosAod inegociosaod;

    @Autowired
    IBeneficiosAod iBeneficiosAod;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "beneficio") Integer beneficio) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        //Long idbeneficio = null;
        try {
            if (beneficio == null || beneficio == 0) {
                mensajes.put("mensaje", "El parámetro 'beneficio' es obligatorio y debe ser mayor que 0.");
                return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
            }

            //idbeneficio = Long.parse(beneficio);

            // if (idbeneficio == 0) {
            //     mensajes.put("mensaje", "El parámetro 'beneficio' debe ser mayor que 0.");
            //     return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
            // }
            datos = inegociosaod.datos(buscar, beneficio.longValue());
        }  catch (NumberFormatException e) {
            mensajes.put("mensaje", "El parámetro 'beneficio' debe ser un número válido.");
            mensajes.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.BAD_REQUEST);
        }  catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }

    @GetMapping(value = "/cantidad")
    ResponseEntity<?> cantidad(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                               @RequestParam(value = "beneficio") Integer beneficio) {
        Integer cantidad = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            cantidad = inegociosaod.cantidad(buscar, beneficio.longValue());
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Integer>(cantidad, HttpStatus.OK);
    }

    @GetMapping("/beneficio")
    ResponseEntity<?> negocio(){
        Beneficios beneficio = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            beneficio = iBeneficiosAod.negocios();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(beneficio, HttpStatus.OK);
    }
}
