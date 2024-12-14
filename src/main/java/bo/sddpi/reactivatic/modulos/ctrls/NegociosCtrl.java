package bo.sddpi.reactivatic.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.INegociosAod;
import bo.sddpi.reactivatic.modulos.entidades.Empresas;
import bo.sddpi.reactivatic.modulos.entidades.Negocios;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/apirest/negocios")
public class NegociosCtrl {

    @Autowired
    INegociosAod inegociosaod;

    @GetMapping
    ResponseEntity<?> datos(@RequestParam(value = "buscar", defaultValue = "") String buscar,
                            @RequestParam(value = "beneficio", defaultValue = "0") String beneficio) {
        List<Negocios> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        Long idbeneficio = null;
        try {
            idbeneficio = Long.parseLong(beneficio);
            datos = inegociosaod.datos(buscar, idbeneficio);
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Negocios>>(datos, HttpStatus.OK);
    }
    
}
