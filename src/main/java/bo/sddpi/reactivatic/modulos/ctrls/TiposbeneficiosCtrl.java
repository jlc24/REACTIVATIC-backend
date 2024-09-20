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
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.aods.ITiposbeneficiosAod;
import bo.sddpi.reactivatic.modulos.entidades.Tiposbeneficios;

@RestController
@RequestMapping("/apirest/tiposbeneficios")
public class TiposbeneficiosCtrl {

    @Autowired
    ITiposbeneficiosAod iTiposbeneficiosAod;

    @GetMapping
    public ResponseEntity<?> listar() {
        List<Tiposbeneficios> tiposDocumentos;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            tiposDocumentos = iTiposbeneficiosAod.listar();
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(tiposDocumentos, HttpStatus.OK);
    }
}
