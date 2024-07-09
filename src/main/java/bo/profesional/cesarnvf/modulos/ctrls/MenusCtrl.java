package bo.profesional.cesarnvf.modulos.ctrls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bo.profesional.cesarnvf.modulos.aods.IMenusAod;
import bo.profesional.cesarnvf.modulos.aods.IUsuariosAod;
import bo.profesional.cesarnvf.modulos.entidades.Menus;

@RestController
@RequestMapping("/apirest/menus")
@PreAuthorize("isAuthenticated()")
public class MenusCtrl {

    @Autowired
    IMenusAod iMenusAod;

    @Autowired
    IUsuariosAod iUsuariosAod;

    @GetMapping
    ResponseEntity<?> datos() {
        List<Menus> datos = null;
        Map<String, Object> mensajes = new HashMap<>();
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            datos = iMenusAod.datos(Long.parseLong(auth.getName()));
        } catch (DataAccessException e) {
            mensajes.put("mensaje", "Error al realizar la consulta en la Base de Datos");
            mensajes.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(mensajes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<Menus>>(datos, HttpStatus.OK);
    }

}