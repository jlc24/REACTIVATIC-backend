package bo.sddpi.reactivatic.modulos.ctrls;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.sddpi.reactivatic.modulos.entidades.Creditos;

@RestController
@RequestMapping("/creditos")
public class IndexCtrl {

    @GetMapping
    ResponseEntity<?> index() {
        Creditos dato = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            dato = new Creditos();
            dato.setAutor("ING. JOSE LUIS LLAMPA COLQUE");
            dato.setProfesion("INGENIERO INFORMATICO");
            dato.setCargo("TECNICO ESPECIALISTA EN TECNOLOGIA - TECNICO GESTION DE LA INFORMACION");
            dato.setAppnombre("REACTIVATICAPP");
            dato.setUnidad("SDDPI - DPEIC");
            dato.setInstitucion("GADOR");
            dato.setFechacreacion(LocalDate.now());
            dato.setVersion("2.0.0");
        } catch (Exception e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Creditos>(dato, HttpStatus.OK);
    }

}